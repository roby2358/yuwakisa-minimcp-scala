package yuwakisa.servel.mcp.handlers.tools

trait Tool:
  def name: String
  def description: String
  def inputSchema: Map[String, Any]
  def outputSchema: Map[String, Any] = Map.empty
  def annotations: Map[String, Any] = Map.empty
  
  def call(input: Map[String, Any]): Map[String, Any]
