package yuwakisa.servel.mcp

import yuwakisa.servel.mcp.handlers.resources.Resource
import yuwakisa.minimcp.resources.CurrentTimeResource

object McpRegistry:
  val resources: List[Resource] = List(new CurrentTimeResource()) 