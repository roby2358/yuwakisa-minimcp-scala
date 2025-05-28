package yuwakisa.servel.mcp.handlers.tools

import yuwakisa.servel.mcp.handlers.MessageHandler
import yuwakisa.servel.mcp.McpMessageTypes.*
import scala.util.Try

class ToolsListHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "tools/list"
  
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try {
      val cursor = request.params.flatMap(_.get("cursor").map(_.toString))
      val (tools, nextCursor) = McpTools.listTools(cursor)
      
      JsonRpcResponse(
        result = Map(
          "tools" -> tools,
          "nextCursor" -> nextCursor
        ),
        id = request.id
      )
    } 