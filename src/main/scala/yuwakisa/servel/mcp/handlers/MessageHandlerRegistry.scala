package yuwakisa.servel.mcp.handlers

import yuwakisa.servel.mcp.handlers.notification.{CancelledNotificationHandler, InitializedNotificationHandler, ProgressNotificationHandler}
import yuwakisa.servel.mcp.handlers.resources.{ResourcesListHandler, ResourcesReadHandler}
import yuwakisa.servel.mcp.handlers.tools.{ToolsListHandler, ToolsCallHandler}
import yuwakisa.servel.mcp.handlers.prompts.{PromptsListHandler, PromptsGetHandler}
import yuwakisa.servel.mcp.McpRegistry

object MessageHandlerRegistry:
  private val handlers: List[MessageHandler] = List(
    new InitializeHandler,
    new PingHandler,
    new CancelledNotificationHandler,
    new ProgressNotificationHandler,
    new ToolsListHandler,
    new ToolsCallHandler,
    new ResourcesListHandler(using McpRegistry.resources),
    new ResourcesReadHandler(using McpRegistry.resources),
    new InitializedNotificationHandler,
    new PromptsListHandler,
    new PromptsGetHandler
  )
  
  def getHandler(method: String): Option[MessageHandler] =
    handlers.find(_.canHandle(method)) 