package yuwakisa.servel.mcp.handlers.tools

import yuwakisa.servel.mcp.handlers.MessageHandler
import yuwakisa.servel.mcp.McpMessageTypes.*
import yuwakisa.servel.mcp.McpTools
import scala.util.{Try, Success, Failure}

class ToolsCallHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "tools/call"
  
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try {
      val name = request.params.flatMap(_.get("name").map(_.toString))
      val arguments = request.params.flatMap(_.get("arguments").map(_.asInstanceOf[Map[String, Any]]))
      
      (name, arguments) match
        case (Some(n), Some(args)) =>
          McpTools.executeTool(n, args) match
            case Success(result) =>
              JsonRpcResponse(
                result = Map(
                  "content" -> result.content,
                  "structuredContent" -> result.structuredContent,
                  "isError" -> result.isError
                ),
                id = request.id
              )
            case Failure(e) =>
              JsonRpcErrorResponse(
                error = JsonRpcError(
                  code = -32603,
                  message = e.getMessage
                ),
                id = request.id
              )
        case _ =>
          JsonRpcErrorResponse(
            error = JsonRpcError(
              code = -32602,
              message = "Missing required parameters: name and arguments"
            ),
            id = request.id
          )
    } 