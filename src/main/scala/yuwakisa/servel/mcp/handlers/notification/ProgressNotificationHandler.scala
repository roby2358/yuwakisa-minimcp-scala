package yuwakisa.servel.mcp.handlers.notification

import yuwakisa.servel.mcp.handlers.MessageHandler
import McpMessageTypes.*
import scala.util.Try

class ProgressNotificationHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "notifications/progress"
  
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try {
      val progressToken = request.params.flatMap(_.get("progressToken").map(_.toString))
      val progress = request.params.flatMap(_.get("progress").map(_.asInstanceOf[Number].doubleValue()))
      val total = request.params.flatMap(_.get("total").map(_.asInstanceOf[Number].doubleValue()))
      val message = request.params.flatMap(_.get("message").map(_.toString))
      
      // TODO: Implement progress tracking logic
      
      JsonRpcResponse(
        result = Map.empty,
        id = request.id
      )
    } 