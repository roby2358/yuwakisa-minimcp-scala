package yuwakisa.servel.mcp

import yuwakisa.servel.mcp.handlers.resources.Resource
import yuwakisa.servel.mcp.handlers.tools.Tool
import yuwakisa.minimcp.resources.CurrentTimeResource
import yuwakisa.minimcp.tools.EchoTool

object McpRegistry:
  val resources: List[Resource] = List(new CurrentTimeResource())
  val tools: List[Tool] = List(new EchoTool()) 