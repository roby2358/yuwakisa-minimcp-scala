package yuwakisa.servel.mcp.handlers

object MessageHandlerRegistry:
  private val handlers: List[MessageHandler] = List(
    new InitializeHandler,
    new PingHandler,
    new CancelledNotificationHandler,
    new ProgressNotificationHandler,
    new ToolsListHandler,
    new ToolsCallHandler,
    new ResourcesListHandler,
    new ResourcesReadHandler,
    new InitializedNotificationHandler
  )
  
  def getHandler(method: String): Option[MessageHandler] =
    handlers.find(_.canHandle(method)) 