package yuwakisa.minimcp

import jakarta.servlet.http.HttpServlet
import yuwakisa.servel.{ServerRunner, StaticContentServlet}
import yuwakisa.servel.Types.Routes
import yuwakisa.servel.mcp.{HealthServlet, McpServlet, ConfigServlet}

object Main:
  val routes: Routes = Map(
    "/" -> classOf[StaticContentServlet],
    "/health" -> classOf[HealthServlet],
    "/config" -> classOf[ConfigServlet],
    "/mcp" -> classOf[McpServlet]
  )

  private val runner = new ServerRunner(
    port = ServerRunner.DefaultPort,
    routes = routes
  )

  def main(args: Array[String]): Unit =
    runner.start()