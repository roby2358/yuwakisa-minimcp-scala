package yuwakisa.minimcp

import yuwakisa.servel.mcp.StdioServer
import yuwakisa.servel.{ServerRunner, StaticContentServlet}
import yuwakisa.servel.mcp.{HealthServlet, McpServlet, ConfigServlet}

object StdioMain:
  def runServer(): Unit =
    val routes = Map(
      "/" -> classOf[StaticContentServlet],
      "/health" -> classOf[HealthServlet],
      "/config" -> classOf[ConfigServlet],
      "/mcp" -> classOf[McpServlet]
    )
    val runner = new ServerRunner(
      port = ServerRunner.DefaultPort,
      routes = routes
    )
    try
      runner.start()
    catch
      case e: Exception =>
        System.err.println(s"Fatal error: ${e.getMessage}")
        e.printStackTrace()
        System.exit(1)

  def runStdio(): Unit =
    val server = new StdioServer()
    try
      server.start()
    catch
      case e: Exception =>
        System.err.println(s"Fatal error: ${e.getMessage}")
        e.printStackTrace()
        System.exit(1)

@main def main(server: Boolean = false): Unit =
  if server then
    StdioMain.runServer()
  else
    StdioMain.runStdio() 