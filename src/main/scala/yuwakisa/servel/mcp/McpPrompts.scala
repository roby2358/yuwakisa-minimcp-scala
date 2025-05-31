package yuwakisa.servel.mcp

object McpPrompts:
  case class PromptArgument(
    name: String,
    description: String,
    required: Boolean,
    schema: Option[Map[String, Any]] = None
  )

  case class Prompt(
    name: String,
    description: String,
    arguments: List[PromptArgument],
    includeContext: String = "none",
    annotations: Option[Map[String, Any]] = None
  )

  case class PromptMessage(
    role: String,
    content: Map[String, Any]
  )

  val prompts = Map(
    "greeting" -> Prompt(
      name = "greeting",
      description = "A simple greeting prompt that takes a name parameter",
      arguments = List(
        PromptArgument(
          name = "name",
          description = "The name to greet",
          required = true,
          schema = Some(Map(
            "type" -> "string"
          ))
        )
      ),
      includeContext = "none"
    )
  )

  def getPrompt(name: String): Option[Prompt] = prompts.get(name)

  def getPromptMessages(name: String, arguments: Map[String, Any]): Option[List[PromptMessage]] =
    prompts.get(name).map { prompt =>
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
    } 