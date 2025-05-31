package yuwakisa.servel.mcp.handlers.resources

import yuwakisa.servel.mcp.handlers.MessageHandler
import yuwakisa.servel.mcp.McpMessageTypes.*
import yuwakisa.servel.mcp.handlers.resources.McpResources

import java.time.Instant
import scala.util.{Failure, Success, Try}

class ResourcesReadHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "resources/read"

  private def readResource(uri: String): Try[List[ResourceContent]] =
    McpResources.resources.values.find(_.uri == uri) match
      case Some(resource) =>
        Success(List(
          ResourceContent.Text(Instant.now().toString)
        ))
      case None =>
        Failure(new IllegalArgumentException(s"Resource not found: $uri"))

  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try:
      val uri = request.params.flatMap(_.get("uri").map(_.toString))
      
      uri match
        case Some(u) =>
          readResource(u) match
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
