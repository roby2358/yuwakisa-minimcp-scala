package yuwakisa.servel.mcp.handlers.resources

import yuwakisa.minimcp.resources.CurrentTimeResource

object McpResources:
  private var _resources: Map[String, Resource] = Map.empty
  
  def resources: Map[String, Resource] = _resources
  
  def initialize(resources: Map[String, Resource]): Unit =
    _resources = resources 