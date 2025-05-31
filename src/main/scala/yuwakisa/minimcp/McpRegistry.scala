package yuwakisa.minimcp

import yuwakisa.minimcp.prompts.GreetingPrompt
import yuwakisa.minimcp.resources.CurrentTimeResource
import yuwakisa.minimcp.tools.EchoTool
import yuwakisa.servel.mcp.handlers.prompts.Prompt
import yuwakisa.servel.mcp.handlers.resources.Resource
import yuwakisa.servel.mcp.handlers.tools.Tool

object McpRegistry:
  val resources: List[Resource] = List(CurrentTimeResource)
  val tools: List[Tool] = List(EchoTool)
  val prompts: List[Prompt] = List(GreetingPrompt) 