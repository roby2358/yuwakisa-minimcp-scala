package yuwakisa.minimcp

import yuwakisa.servel.{ServerRunner, StaticContentServlet}
import yuwakisa.servel.Types.Routes
import yuwakisa.servel.mcp.{HealthServlet, McpServlet, ConfigServlet, McpRegistry}

object Main:
  val routes: Routes = Map(
    "/" -> classOf[StaticContentServlet],
    "/health" -> classOf[HealthServlet],
    "/config" -> classOf[ConfigServlet],
    "/mcp" -> classOf[McpServlet]
  )

  def main(args: Array[String]): Unit =
    val runner = new ServerRunner(
        port = ServerRunner.DefaultPort,
        routes = routes
      )

    runner.start()
