package yuwakisa.servel.mcp.handlers.resources

import yuwakisa.servel.mcp.handlers.MessageHandler
import yuwakisa.servel.mcp.McpMessageTypes.*
import yuwakisa.servel.mcp.McpResources
import scala.util.Try

class ResourcesListHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "resources/list"
  
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try {
      val cursor = request.params.flatMap(_.get("cursor").map(_.toString))
      val (resources, nextCursor) = McpResources.listResources(cursor)
      
      val result = Map("resources" -> resources) ++ 
        nextCursor.map(cursor => Map("nextCursor" -> cursor)).getOrElse(Map.empty)
      
      JsonRpcResponse(
        result = result,
        id = request.id
      )
    } 