package yuwakisa.servel.mcp.handlers.resources

import yuwakisa.servel.mcp.handlers.MessageHandler
import yuwakisa.servel.mcp.McpMessageTypes.*
import yuwakisa.servel.mcp.McpResources
import yuwakisa.servel.mcp.ResourceContent
import scala.util.{Try, Success, Failure}

class ResourcesReadHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "resources/read"
  
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try {
      val uri = request.params.flatMap(_.get("uri").map(_.toString))
      
      uri match
        case Some(u) =>
          McpResources.readResource(u) match
            case Success(contents) =>
              JsonRpcResponse(
                result = Map(
                  "contents" -> contents.map {
                    case ResourceContent.Text(text) =>
                      Map("uri" -> "resource://current_time", "text" -> text)
                    case ResourceContent.Blob(data, mimeType) =>
                      Map("uri" -> "resource://current_time", "blob" -> data, "mimeType" -> mimeType)
                  }
                ),
                id = request.id
              )
            case Failure(e) =>
              JsonRpcErrorResponse(
                error = JsonRpcError(
                  code = -32002,
                  message = e.getMessage
                ),
                id = request.id
              )
        case None =>
          JsonRpcErrorResponse(
            error = JsonRpcError(
              code = -32602,
              message = "Missing required parameter: uri"
            ),
            id = request.id
          )
    } 