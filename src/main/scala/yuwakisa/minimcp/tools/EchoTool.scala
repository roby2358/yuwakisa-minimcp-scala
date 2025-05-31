package yuwakisa.minimcp.tools

import yuwakisa.servel.mcp.handlers.tools.Tool

object EchoTool extends Tool {
  val name: String = "echo"
  
  val description: String = "Echoes back the input message"
  
  val annotations: Map[String, Any] = Map.empty
  
  val inputSchema: Map[String, Any] = Map(
    "type" -> "object",
    "properties" -> Map(
      "message" -> Map(
        "type" -> "string",
        "description" -> "The message to echo back"
      )
    ),
    "required" -> List("message")
  )
  
  val outputSchema: Map[String, Any] = Map(
    "type" -> "object",
    "properties" -> Map(
      "content" -> Map(
        "type" -> "array",
        "minItems" -> 1,
        "items" -> Map(
          "type" -> "object",
          "properties" -> Map(
            "type" -> Map(
              "type" -> "string",
              "enum" -> List("text")
            ),
            "text" -> Map("type" -> "string")
          ),
          "required" -> List("type", "text")
        )
      ),
      "structuredContent" -> Map(
        "type" -> "object",
        "properties" -> Map.empty
      ),
      "isError" -> Map("type" -> "boolean")
    ),
    "required" -> List("content", "structuredContent", "isError")
  )
  
  def call(input: Map[String, Any]): Map[String, Any] = {
    input.get("message") match {
      case Some(message) if message.isInstanceOf[String] =>
        Map(
          "content" -> List(
            Map(
              "type" -> "text",
              "text" -> s"Echo: $message"
            )
          ),
          "structuredContent" -> Map.empty,
          "isError" -> false
        )
      case _ =>
        Map(
          "content" -> List(
            Map(
              "type" -> "text",
              "text" -> "Error: Missing required argument: message"
            )
          ),
          "structuredContent" -> Map.empty,
          "isError" -> true
        )
    }
  }
} 