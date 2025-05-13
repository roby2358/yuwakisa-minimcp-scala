package yuwakisa.minimcp

import yuwakisa.servel.{HelloWorldServlet, ServerRunner, StaticContentServlet}

object Main:
  val routes = Map( "/" -> classOf[StaticContentServlet],
    "/hello" -> classOf[HelloWorldServlet],
  )

  val runner = new ServerRunner(ServerRunner.DefaultPort, routes)

  def main(args: Array[String]): Unit =
    runner.start()