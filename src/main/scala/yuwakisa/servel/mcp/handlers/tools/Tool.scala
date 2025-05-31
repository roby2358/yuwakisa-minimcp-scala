package yuwakisa.servel.mcp.handlers.tools

case class ToolResult(
                       content: List[Map[String, Any]], // Must have at least one item
                       structuredContent: Map[String, Any] = Map.empty,
                       isError: Boolean = false
                     )

trait Tool:
  def name: String
  def description: String
  def inputSchema: Map[String, Any]
  def outputSchema: Map[String, Any] = Map.empty
  def annotations: Map[String, Any] = Map.empty
  
  def call(input: Map[String, Any]): Map[String, Any]
