package yuwakisa.minimcp

import jakarta.servlet.http.HttpServlet
import yuwakisa.servel.{HelloWorldServlet, ServerRunner, StaticContentServlet}
import yuwakisa.servel.Types.Routes

object Main:
  val routes: Routes = Map(
    "/" -> classOf[StaticContentServlet],
    "/hello" -> classOf[HelloWorldServlet],
  )

  private val runner = new ServerRunner(ServerRunner.DefaultPort, routes)

  def main(args: Array[String]): Unit =
    runner.start()