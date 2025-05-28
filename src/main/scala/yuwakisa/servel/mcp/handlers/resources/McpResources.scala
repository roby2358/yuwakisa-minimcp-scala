package yuwakisa.servel.mcp.handlers.resources

import yuwakisa.servel.mcp.{Resource, ResourceContent}

import java.time.Instant
import scala.util.{Failure, Success, Try}

object McpResources:
  private val resources = Map(
    "current_time" -> Resource(
      uri = "resource://current_time",
      name = "Current Time",
      description = Some("Returns the current server time"),
      mimeType = Some("text/plain")
    )
  )

  def listResources(cursor: Option[String]): (List[Resource], Option[String]) =
    // Simple pagination - just return all resources for now
    (resources.values.toList, None)

  def readResource(uri: String): Try[List[ResourceContent]] =
    uri match
      case "resource://current_time" =>
        Success(List(
          ResourceContent.Text(Instant.now().toString)
        ))
      case _ =>
        Failure(new IllegalArgumentException(s"Resource not found: $uri")) 