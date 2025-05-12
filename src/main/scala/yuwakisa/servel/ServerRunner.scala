package yuwakisa.servel

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.ee10.servlet.{ServletContextHandler, ServletHolder}
import jakarta.servlet.http.HttpServlet

object ServerRunner:
  lazy val Port = 8889
  lazy val Server = new Server(Port)

class ServerRunner(routes: Map[String, Class[? <: jakarta.servlet.Servlet]]):

  def start(): Unit =
    ctrlCHook()

    // Create a ServletContextHandler with context path "/"
    val context = new ServletContextHandler(ServletContextHandler.SESSIONS)
    context.setContextPath("/")

    // Add servlets to the context
    routes.foreach { case (path: String, servletClass: Class[? <: HttpServlet]) =>
      context.addServlet(new ServletHolder(servletClass), path)
    }

    // Set the context as the handler for the server
    ServerRunner.Server.setHandler(context)

    // Start the server
    ServerRunner.Server.start()
    println(s"Server started on localhost:${ServerRunner.Port}")
    println("Press Enter to stop the server")
    scala.io.StdIn.readLine()
    println(s"Stopping")
    ServerRunner.Server.stop()

  def ctrlCHook(): Unit =
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run(): Unit = {
        println("Shutdown hook")
        // if (socket != null) socket.close()
        Thread.sleep(500)
      }
    })