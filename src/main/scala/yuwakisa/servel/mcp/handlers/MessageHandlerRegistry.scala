package yuwakisa.servel.mcp.handlers

import yuwakisa.servel.mcp.handlers.notification.{CancelledNotificationHandler, InitializedNotificationHandler, ProgressNotificationHandler}
import yuwakisa.servel.mcp.handlers.resources.{ResourcesListHandler, ResourcesReadHandler}
import yuwakisa.servel.mcp.handlers.tools.{ToolsListHandler, ToolsCallHandler}
import yuwakisa.servel.mcp.handlers.prompts.{PromptsListHandler, PromptsGetHandler}
import yuwakisa.servel.mcp.McpRegistry
import yuwakisa.servel.mcp.handlers.{InitializeHandler, PingHandler}

object MessageHandlerRegistry:
  private val handlers: List[MessageHandler] = List(
    new CancelledNotificationHandler,
    new InitializedNotificationHandler,
    new InitializeHandler,
    new PingHandler,
    new ProgressNotificationHandler,
    new PromptsGetHandler(using McpRegistry.prompts),
    new PromptsListHandler(using McpRegistry.prompts),
    new ResourcesListHandler(using McpRegistry.resources),
    new ResourcesReadHandler(using McpRegistry.resources),
    new ToolsCallHandler,
    new ToolsListHandler,
  )
  
  def getHandler(method: String): Option[MessageHandler] =
    handlers.find(_.canHandle(method)) 