package yuwakisa.servel.mcp

import yuwakisa.servel.mcp.handlers.resources.Resource

object McpRegistry:
  private var _resources: List[Resource] = List.empty
  
  def resources: List[Resource] = _resources
  
  def registerResource(resource: Resource): Unit =
    _resources = resource :: _resources
    
  def clearResources(): Unit =
    _resources = List.empty 