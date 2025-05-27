package yuwakisa.servel.mcp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import scala.util.{Try, Success, Failure}
import McpMessageTypes.*

trait MessageHandler:
  def canHandle(method: String): Boolean
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage]

class InitializeHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "initialize"
  
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try {
      val params = request.params.getOrElse(Map.empty)
      val capabilities = params.get("capabilities").map(_.asInstanceOf[Map[String, Any]]).getOrElse(Map.empty)
      
      // TODO: Validate protocol version and other params
      
      JsonRpcResponse(
        result = Map(
          "protocolVersion" -> "2025-03-26",
          "capabilities" -> Map(
            "roots" -> Map("listChanged" -> true),
            "resources" -> Map("listChanged" -> true, "subscribe" -> true),
            "tools" -> Map("listChanged" -> true),
            "prompts" -> Map("listChanged" -> true),
            "sampling" -> Map(),
            "logging" -> Map(),
            "experimental" -> Map()
          ),
          "serverInfo" -> Map(
            "name" -> "Servel MCP Server",
            "version" -> "0.1.0"
          )
        ),
        id = request.id
      )
    }

class PingHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "ping"
  
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try {
      JsonRpcResponse(
        result = Map.empty,
        id = request.id
      )
    }

class CancelledNotificationHandler extends MessageHandler:
  def canHandle(method: String): Boolean = method == "notifications/cancelled"
  
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] =
    Try {
      val requestId = request.params.flatMap(_.get("requestId").map(_.toString))
      val reason = request.params.flatMap(_.get("reason").map(_.toString))
      
      // TODO: Implement cancellation logic
      
      JsonRpcResponse(
        result = Map.empty,
        id = request.id
      )
    }

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

object MessageHandlerRegistry:
  private val handlers: List[MessageHandler] = List(
    new InitializeHandler,
    new PingHandler,
    new CancelledNotificationHandler,
    new ProgressNotificationHandler,
    new ToolsListHandler,
    new ToolsCallHandler,
    new ResourcesListHandler,
    new ResourcesReadHandler,
    new InitializedNotificationHandler
  )
  
  def getHandler(method: String): Option[MessageHandler] =
    handlers.find(_.canHandle(method)) 