package yuwakisa.servel

import org.eclipse.jetty.server.Server
import org.eclipse.jetty.ee10.servlet.ServletHandler

import jakarta.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

object ServerRunner :
  lazy val Port = 8888
  lazy val Server = new Server(Port)
  lazy val Handler = new ServletHandler()

class ServerRunner(routes: Map[String, Class[? <: jakarta.servlet.Servlet]]):

  def start(): Unit =
    ctrlCHook()

    routes.foreach { case (p: String, c: Class[? <: jakarta.servlet.Servlet]) =>
      ServerRunner.Handler.addServletWithMapping(c, p) }
    ServerRunner.Server.setHandler(ServerRunner.Handler)
    ServerRunner.Server.start()
    println(s"Server started on localhost:${ServerRunner.Port}")
    println("Press Enter to stop the server")
    scala.io.StdIn.readLine()
    println(s"Stopping")
    ServerRunner.Server.stop()

  def ctrlCHook(): Unit =
    Runtime.getRuntime.addShutdownHook(new Thread {
      override def run() = {
        println("Shutdown hook")
        // if (socket != null) socket.close()
        Thread.sleep(500)
      }
    })