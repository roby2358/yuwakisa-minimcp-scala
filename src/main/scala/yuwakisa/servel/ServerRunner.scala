package yuwakisa.servel

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.ee10.servlet.{ServletContextHandler, ServletHolder}
import jakarta.servlet.http.HttpServlet

object ServerRunner:
  lazy val Port = 8889
  private val logger = Logger(ServerRunner.getClass)

class ServerRunner(routes: Map[String, Class[? <: jakarta.servlet.Servlet]]):
  private val logger = Logger(this.getClass)

  // Make these methods protected so they can be overridden in tests
  protected def createServer(port: Int): Server = new Server(port)
  protected def createContext(): ServletContextHandler =
    new ServletContextHandler(ServletContextHandler.SESSIONS)

  private lazy val server = createServer(ServerRunner.Port)

  def start(): Unit =
    ctrlCHook()

    // Create a ServletContextHandler with context path "/"
    val context = createContext()
    context.setContextPath("/")

    // Add servlets to the context
    routes.foreach { case (path: String, servletClass: Class[? <: HttpServlet]) =>
      logger.debug(s"Adding servlet mapping: $path -> ${servletClass.getName}")
      context.addServlet(new ServletHolder(servletClass), path)
    }

    // Set the context as the handler for the server
    server.setHandler(context)

    // Start the server
    logger.info(s"Starting server on port ${ServerRunner.Port}")
    server.start()
    logger.info(s"Server started on localhost:${ServerRunner.Port}")
    println(s"Server started on localhost:${ServerRunner.Port}")
    println("Press Enter to stop the server")
    scala.io.StdIn.readLine()
    logger.info("Server shutdown requested")
    println("Stopping")
    server.stop()
    logger.info("Server stopped")

  def ctrlCHook(): Unit =
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit = {
        logger.info("Shutdown hook triggered")
        try {
          if (server.isRunning) {
            logger.info("Stopping server from shutdown hook")
            server.stop()
          }
        } catch {
          case e: Exception => logger.error("Error stopping server in shutdown hook", e)
        }
        Thread.sleep(500)
      }
    })