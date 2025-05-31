package yuwakisa.servel.mcp

import yuwakisa.servel.mcp.handlers.resources.Resource
import yuwakisa.servel.mcp.handlers.tools.Tool
import yuwakisa.minimcp.resources.CurrentTimeResource
import yuwakisa.minimcp.tools.EchoTool
import yuwakisa.minimcp.prompts.GreetingPrompt
import yuwakisa.servel.mcp.handlers.prompts.{Prompt, PromptMessage}

object McpRegistry:
  val resources: List[Resource] = List(new CurrentTimeResource())
  val tools: List[Tool] = List(new EchoTool())
  val prompts: List[Prompt] = List(GreetingPrompt)

  given McpRegistry.type = this 