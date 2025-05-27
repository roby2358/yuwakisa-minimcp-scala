package yuwakisa.servel.mcp.handlers

import McpMessageTypes.*

trait MessageHandler:
  def canHandle(method: String): Boolean
  def handle(request: JsonRpcRequest): Try[JsonRpcMessage] 