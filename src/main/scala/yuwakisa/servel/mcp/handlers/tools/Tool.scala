package yuwakisa.servel.mcp.handlers.tools

case class Tool(
  name: String,
  description: String,
  inputSchema: Map[String, Any],
  outputSchema: Map[String, Any] = Map.empty,
  annotations: Map[String, Any] = Map.empty
)

