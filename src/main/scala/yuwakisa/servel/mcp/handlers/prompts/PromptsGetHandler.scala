package yuwakisa.servel.mcp.handlers.prompts

import yuwakisa.servel.mcp.handlers.MessageHandler
import yuwakisa.servel.mcp.McpMessageTypes.*
import yuwakisa.servel.mcp.McpPrompts.*

import scala.util.{Failure, Success, Try}

// Note: Message roles are currently limited to "user" and "assistant" for compatibility
// with existing clients. The original spec included "system" as a valid role, but this
// was removed to match client validation requirements.
class PromptsGetHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "prompts/get"

  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try:
      val params = request.params.getOrElse(Map.empty)
      val name = params.get("name").map(_.toString)
      val arguments = params.get("arguments").map(_.asInstanceOf[Map[String, Any]]).getOrElse(Map.empty)
      
      name match
        case Some(n) =>
          getPromptMessages(n, arguments) match
            case Some(messages) =>
              JsonRpcResponse(
                result = Map(
                  "messages" -> messages,
                  "includeContext" -> "none"
                ),
                id = request.id
              )
            case None =>
              JsonRpcErrorResponse(
                error = JsonRpcError(
                  code = -32001,
                  message = s"Prompt not found: $n"
                ),
                id = request.id
              )
        case None =>
          JsonRpcErrorResponse(
            error = JsonRpcError(
              code = -32602,
              message = "Missing required parameter: name"
            ),
            id = request.id
          ) 