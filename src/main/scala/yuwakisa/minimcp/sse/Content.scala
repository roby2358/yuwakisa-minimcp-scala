package yuwakisa.minimcp.sse

import jakarta.servlet.http.HttpServletResponse
import yuwakisa.servel.Content.Mapper

object Content:
  private val JsonRpcVersion = "2.0"

  private object MimeType:
    val Sse = "text/event-stream"

  def okJsonSse(response: HttpServletResponse, obj: Map[String, Any]): Unit =
    val newObj = obj + ("jsonrpc" -> JsonRpcVersion)
    val json = Mapper.writeValueAsString(newObj)
    response.setContentType(MimeType.Sse)
    response.setStatus(HttpServletResponse.SC_OK)
    response.getWriter.println(json)