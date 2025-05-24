package yuwakisa.minimcp

import jakarta.servlet.http.HttpServlet
import yuwakisa.servel.{ServerRunner, StaticContentServlet}
import yuwakisa.servel.Types.Routes
import yuwakisa.servel.mcp.HealthServlet

object Main:
  val routes: Routes = Map(
    "/" -> classOf[StaticContentServlet],
    "/health" -> classOf[HealthServlet],
  )

  private val runner = new ServerRunner(ServerRunner.DefaultPort, routes)

  def main(args: Array[String]): Unit =
    runner.start()