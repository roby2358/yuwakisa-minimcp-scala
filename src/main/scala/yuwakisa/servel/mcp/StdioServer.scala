package yuwakisa.servel.mcp

import scala.util.{Try, Success, Failure}
import McpMessageTypes.*
import yuwakisa.servel.mcp.handlers.MessageHandlerRegistry
import java.io.IOException

class StdioServer:
  private val transport = new StdioTransport()
  private var isRunning = false

  def start(): Unit =
    isRunning = true
    while isRunning do
      transport.readMessage() match
        case Success(request) =>
          handleRequest(request)
        case Failure(e) =>
          e match
            case _: IllegalStateException if e.getMessage == "End of input stream" =>
              isRunning = false
            case _ =>
              transport.writeError(s"Failed to read message: ${e.getMessage}")
              // Continue running despite the error

  private def handleRequest(request: JsonRpcRequest): Unit =
    MessageHandlerRegistry.getHandler(request.method) match
      case Some(handler) =>
        handler.handle(request) match
          case Success(Some(response)) =>
            transport.writeMessage(response) match
              case Success(_) => // Message sent successfully
              case Failure(e) =>
                transport.writeError(s"Failed to send response: ${e.getMessage}")
                // Continue running despite the error
          case Success(None) =>
            // No response needed for notifications
            ()
          case Failure(e) =>
            val errorResponse = JsonRpcErrorResponse(
              error = JsonRpcError(
                code = -32603,
                message = s"Internal error: ${e.getMessage}"
              ),
              id = request.id
            )
            transport.writeMessage(errorResponse) match
              case Success(_) => // Error response sent successfully
              case Failure(e) =>
                transport.writeError(s"Failed to send error response: ${e.getMessage}")
                // Continue running despite the error
      case None =>
        val errorResponse = JsonRpcErrorResponse(
          error = JsonRpcError(
            code = -32601,
            message = s"Method not found: ${request.method}"
          ),
          id = request.id
        )
        transport.writeMessage(errorResponse) match
          case Success(_) => // Error response sent successfully
          case Failure(e) =>
            transport.writeError(s"Failed to send error response: ${e.getMessage}")
            // Continue running despite the error

  def stop(): Unit =
    isRunning = false 