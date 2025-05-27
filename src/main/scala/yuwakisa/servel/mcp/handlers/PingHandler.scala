package yuwakisa.servel.mcp.handlers

import McpMessageTypes.*
import scala.util.Try

class PingHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "ping"
  
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try {
      JsonRpcResponse(
        result = Map.empty,
        id = request.id
      )
    } 