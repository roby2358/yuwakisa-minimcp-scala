package yuwakisa.minimcp.resources

import yuwakisa.servel.mcp.handlers.resources.{Resource, ResourceContent}

import java.time.Instant
import scala.util.{Success, Try}

class CurrentTimeResource extends Resource:
  val uri = "resource://current_time"
  val name = "Current Time"
  val description: Option[String] = Some("Returns the current server time")
  val mimeType: Option[String] = Some("text/plain")
  
  def read(): Try[List[ResourceContent]] =
    Success(List(ResourceContent.Text(Instant.now().toString))) 