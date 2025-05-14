package yuwakisa.servel

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.ee10.servlet.{ServletContextHandler, ServletHolder}
import jakarta.servlet.http.HttpServlet

object ServerRunner:
  val DefaultPort = 8889

class ServerRunner(port:Int, routes: Map[String, Class[? <: jakarta.servlet.Servlet]]):
  private val logger = this.getLogger

  private def createContext(): ServletContextHandler =
    new ServletContextHandler(ServletContextHandler.SESSIONS)
  private def createServer(port: Int): Server = new Server(port)

  private lazy val server = createServer(port)

  def start(): Unit =
    ctrlCHook()

    // Create a ServletContextHandler with context path "/"
    val context = createContext()
    context.setContextPath("/")

    // Add servlets to the context
    routes.foreach:
      case (path: String, servletClass: Class[? <: HttpServlet]) =>
        context.addServlet(new ServletHolder(servletClass), path)

    // Set the context as the handler for the server
    server.setHandler(context)

    // Start the server
    server.start()
    println(s"Server started on localhost:$port")
    println("Press Enter to stop the server")
    scala.io.StdIn.readLine()
    println("Server shutdown requested")
    server.stop()
    println("Server stopped")

  def stop(): Unit =
    logger.debug("Forced stop")
    server.stop()

  private def ctrlCHook(): Unit =
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit = {
        logger.debug("Shutdown hook triggered")
        try {
          if (server.isRunning) {
            logger.debug("Stopping server from shutdown hook")
            server.stop()
          }
        } catch {
          case e: Exception => logger.error("Error stopping server in shutdown hook", e)
        }
        Thread.sleep(500)
      }
    })