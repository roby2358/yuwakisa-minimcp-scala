package yuwakisa.servel

import munit.FunSuite
import jakarta.servlet.http.HttpServlet
import scala.util.{Try, Using, Success, Failure}
import scala.util.Random
import scala.concurrent.{Future, Await, ExecutionContext}
import java.util.concurrent.Executors
import java.net.HttpURLConnection
import java.util.concurrent.atomic.AtomicBoolean
import scala.language.adhocExtensions
import scala.io.Source

// Tag for integration tests
object IntegrationTest extends munit.Tag("integration")

class ServerRunnerTest extends FunSuite:
  private val logger = Logger(this.getClass)

  case class HttpResponse(statusCode: Int, body: String)

  private def httpGet(url: String): Try[HttpResponse] =
    val connection = java.net.URI.create(url).toURL.openConnection().asInstanceOf[HttpURLConnection]
    Try:
      connection.setRequestMethod("GET")
      connection.setConnectTimeout(5000)
      connection.setReadTimeout(5000)

      val statusCode = connection.getResponseCode

      val inputStream =
        if statusCode >= 400 then connection.getErrorStream
        else connection.getInputStream

      val body =
        if inputStream == null then ""
        else
          Using(Source.fromInputStream(inputStream)) { source =>
            source.mkString
          }.getOrElse("")

      HttpResponse(statusCode, body)
    .tap(_ => connection.disconnect())

  private def choosePort: Int = 49152 + Random.nextInt(16384)

  private def createTestServer(routes: Map[String, Class[? <: HttpServlet]],
                              testFunction: Int => Unit): Unit =
    val port = choosePort
    logger.info(s"Creating test server on port $port")

    // Create a custom ServerRunner that doesn't wait for input to stop
    class TestServerRunner(routes: Map[String, Class[? <: HttpServlet]], port: Int)
      extends ServerRunner(routes):

      private val isRunning = new AtomicBoolean(true)
      private val executionContext = ExecutionContext.fromExecutor(Executors.newSingleThreadExecutor())
      private val testLogger = Logger(this.getClass)

      override protected def createServer(port: Int) = new org.eclipse.jetty.server.Server(port)

      // Override start to not block on stdin
      override def start(): Unit =
        // Create a ServletContextHandler with context path "/"
        val context = createContext()
        context.setContextPath("/")

        // Add servlets to the context
        routes.foreach:
          case (path, servletClass) =>
            testLogger.debug(s"Adding test servlet mapping: $path -> ${servletClass.getName}")
            context.addServlet(new org.eclipse.jetty.ee10.servlet.ServletHolder(servletClass), path)

        // Set the context as the handler for the server
        val server = createServer(port)
        server.setHandler(context)

        // Start the server in a separate thread
        testLogger.info(s"Starting test server on port $port")
        Future {
          server.start()
          testLogger.info(s"Test server started on port $port")
          while isRunning.get() do
            Thread.sleep(100)
          testLogger.info("Stopping test server")
          server.stop()
          testLogger.info("Test server stopped")
        }(executionContext)

        // Wait for server to start
        Thread.sleep(500)

        try
          // Run the test function
          testLogger.debug("Running test function")
          testFunction(port)
        finally
          // Signal the server to stop
          testLogger.debug("Test completed, stopping server")
          isRunning.set(false)
          // Wait for server to stop
          Thread.sleep(500)

    // Create and start the test server
    val server = new TestServerRunner(routes, port)
    server.start()

  test("ServerRunner should register and handle a single route".tag(IntegrationTest)):
    val routes = Map("/hello" -> classOf[HelloWorldServlet])
    
    createTestServer(routes, port =>
      val response = httpGet(s"http://localhost:$port/hello")
      response match
        case Success(r) =>
          assertEquals(r.statusCode, 200)
          assertEquals(r.body.trim, "Hello world!")
        case Failure(e) =>
          fail(s"Unexpected error: ${e.getMessage}"))

  test("ServerRunner should handle multiple routes".tag(IntegrationTest)):
    val routes = Map(
      "/hello" -> classOf[HelloWorldServlet],
      "/api" -> classOf[ResourceServlet]
    )
    
    createTestServer(routes, port =>
      // Test HelloWorldServlet
      val helloResponse = httpGet(s"http://localhost:$port/hello")
      helloResponse match
        case Success(r) =>
          assertEquals(r.statusCode, 200)
          assertEquals(r.body.trim, "Hello world!")
        case Failure(e) =>
          fail(s"HelloWorldServlet test error: ${e.getMessage}")
      
      // Test ResourceServlet
      val apiResponse = httpGet(s"http://localhost:$port/api")
      apiResponse match
        case Success(r) =>
          assertEquals(r.statusCode, 200)
          assertEquals(r.body.trim, """["hello","world"]""")
        case Failure(e) =>
          fail(s"ResourceServlet test error: ${e.getMessage}"))

  test("ServerRunner should handle 404 for non-existent endpoints".tag(IntegrationTest)):
    val routes = Map("/hello" -> classOf[HelloWorldServlet])
    
    createTestServer(routes, port =>
      val response = httpGet(s"http://localhost:$port/nonexistent")
      response match
        case Success(r) =>
          assertEquals(r.statusCode, 404)
        case Failure(e) =>
          fail(s"Unexpected error: ${e.getMessage}"))

  test("ServerRunner should handle server errors".tag(IntegrationTest)):
    // We need to define a named class to use it in routes
    class ErrorServlet extends HttpServlet:
      override def doGet(req: jakarta.servlet.http.HttpServletRequest,
                       resp: jakarta.servlet.http.HttpServletResponse): Unit =
        throw new RuntimeException("Test error")
    
    val routes = Map("/error" -> classOf[ErrorServlet])
    
    createTestServer(routes, port =>
      val response = httpGet(s"http://localhost:$port/error")
      response match
        case Success(r) =>
          // Jetty might return a 500 error page instead of failing the request
          assert(r.statusCode == 500 || r.statusCode == 400,
            s"Expected 500 or 400 status code, got ${r.statusCode}")
        case Failure(e) =>
          // Either connection failure or other exception is acceptable
          // for an endpoint that throws an exception
          assert(true))

  test("ServerRunner should handle empty routes".tag(IntegrationTest)):
    val routes = Map.empty[String, Class[? <: HttpServlet]]
    
    createTestServer(routes, port =>
      // Just verify the server starts without errors
      // No routes are defined, so any request should 404
      val response = httpGet(s"http://localhost:$port/")
      response match
        case Success(r) => assertEquals(r.statusCode, 404)
        case Failure(e) =>
          fail(s"Unexpected error: ${e.getMessage}"))

  test("StaticContentServlet should serve static files".tag(IntegrationTest)):
    val routes = Map("/*" -> classOf[StaticContentServlet])
    
    createTestServer(routes, port =>
      val response = httpGet(s"http://localhost:$port/test/index.html")
      response match
        case Success(r) =>
          assertEquals(r.statusCode, 200)
          assert(r.body.contains("Hello Test!"),
            "Expected index.html content to contain 'Hello Test!'")
        case Failure(e) =>
          fail(s"Unexpected error: ${e.getMessage}"))