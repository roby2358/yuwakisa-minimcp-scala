package yuwakisa.servel.mcp

import scala.util.{Try, Success, Failure}
import McpMessageTypes.*
import yuwakisa.servel.mcp.handlers.MessageHandlerRegistry

class StdioServer:
  private val transport = new StdioTransport()
  private var isRunning = false

  def start(): Unit =
    isRunning = true
    try
      while isRunning do
        transport.readMessage() match
          case Success(request) =>
            handleRequest(request)
          case Failure(e) =>
            transport.writeError(s"Failed to read message: ${e.getMessage}")
            isRunning = false
    finally
      transport.close()

  private def handleRequest(request: JsonRpcRequest): Unit =
    MessageHandlerRegistry.getHandler(request.method) match
      case Some(handler) =>
        handler.handle(request) match
          case Success(response) =>
            transport.writeMessage(response)
          case Failure(e) =>
            val errorResponse = JsonRpcErrorResponse(
              error = JsonRpcError(
                code = -32603,
                message = s"Internal error: ${e.getMessage}"
              ),
              id = request.id
            )
            transport.writeMessage(errorResponse)
      case None =>
        val errorResponse = JsonRpcErrorResponse(
          error = JsonRpcError(
            code = -32601,
            message = s"Method not found: ${request.method}"
          ),
          id = request.id
        )
        transport.writeMessage(errorResponse)

  def stop(): Unit =
    isRunning = false 