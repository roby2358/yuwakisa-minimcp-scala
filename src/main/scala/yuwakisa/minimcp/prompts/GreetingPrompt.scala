package yuwakisa.minimcp.prompts

import yuwakisa.servel.mcp.handlers.prompts.{Prompt, PromptArgument, PromptMessage}

object GreetingPrompt extends Prompt:
  val name = "greeting"
  val description = "A simple greeting prompt that takes a name parameter"
  val arguments = List(
    PromptArgument(
      name = "name",
      description = "The name to greet",
      required = true,
      schema = Some(Map(
        "type" -> "string"
      ))
    )
  )
  override val includeContext = "none"

  def getMessages(arguments: Map[String, Any]): List[PromptMessage] =
    val name = arguments.get("name").map(_.toString).getOrElse("there")
    List(
      PromptMessage(
        role = "assistant",
        content = Map(
          "type" -> "text",
          "text" -> "I am a friendly assistant."
        )
      ),
      PromptMessage(
        role = "user",
        content = Map(
          "type" -> "text",
          "text" -> s"Hello $name! How are you today?"
        )
      )
    ) 