package yuwakisa.servel.mcp

case class Resource(
  uri: String,
  name: String,
  description: Option[String] = None,
  mimeType: Option[String] = None
) 