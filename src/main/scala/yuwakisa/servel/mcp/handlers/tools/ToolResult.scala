package yuwakisa.servel.mcp.handlers.tools

case class ToolResult(
  content: List[Map[String, Any]],  // Must have at least one item
  structuredContent: Map[String, Any] = Map.empty,
  isError: Boolean = false
) 