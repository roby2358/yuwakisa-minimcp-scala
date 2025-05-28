package yuwakisa.servel.mcp.handlers.tools

import yuwakisa.servel.mcp.handlers.tools.Tool

import yuwakisa.servel.mcp.handlers.tools.ToolResult

import scala.util.{Failure, Success, Try}

object McpTools:
  private val tools = Map(
    "echo" -> Tool(
      name = "echo",
      description = "Echoes back the input message",
      inputSchema = Map(
        "type" -> "object",
        "properties" -> Map(
          "message" -> Map(
            "type" -> "string",
            "description" -> "The message to echo back"
          )
        ),
        "required" -> List("message")
      ),
      outputSchema = Map(
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
    )
  )

  def listTools(cursor: Option[String]): (List[Tool], String) =
    // Simple pagination - just return all tools for now
    (tools.values.toList, "")

  def executeTool(name: String, arguments: Map[String, Any]): Try[ToolResult] =
    tools.get(name) match
      case Some(tool) =>
        name match
          case "echo" =>
            arguments.get("message") match
              case Some(message) if message.isInstanceOf[String] =>
                // Ensure content array has at least one item
                val content = List(
                  Map(
                    "type" -> "text",
                    "text" -> s"Echo: $message"
                  )
                )
                Success(ToolResult(
                  content = content,
                  structuredContent = Map.empty,
                  isError = false
                ))
              case _ =>
                Failure(new IllegalArgumentException("Missing required argument: message"))
          case _ =>
            Failure(new IllegalArgumentException(s"Tool not implemented: $name"))
      case None =>
        Failure(new IllegalArgumentException(s"Tool not found: $name")) 