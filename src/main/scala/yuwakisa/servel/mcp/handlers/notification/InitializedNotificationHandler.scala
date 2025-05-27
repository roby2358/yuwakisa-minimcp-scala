package yuwakisa.servel.mcp.handlers.notification

import yuwakisa.servel.mcp.handlers.MessageHandler
import McpMessageTypes.*
import scala.util.Try

class InitializedNotificationHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "notifications/initialized"
  
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try {
      // This is a notification, so we return an empty response
      JsonRpcResponse(
        result = Map.empty,
        id = request.id
      )
    } 