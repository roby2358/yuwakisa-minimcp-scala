package yuwakisa.minimcp.stream

import jakarta.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}
import java.io.PrintWriter
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import scala.util.Using

class StreamServlet extends HttpServlet:
  private val mapper = ObjectMapper()
    .registerModule(DefaultScalaModule)

  override def doGet(request: HttpServletRequest, response: HttpServletResponse): Unit =
    // Set headers for streaming
    response.setContentType("application/x-ndjson")  // Newline-delimited JSON
    response.setHeader("Cache-Control", "no-cache")
    response.setHeader("Connection", "keep-alive")
    response.setStatus(HttpServletResponse.SC_OK)

    // Get the writer and ensure it's not buffered
    val writer = response.getWriter
    response.flushBuffer()

    // Example: Send a stream of messages
    try
      for i <- 1 to 3 do
        val message = Map(
          "type" -> "message",
          "id" -> i,
          "content" -> s"Message $i"
        )
        writer.println(mapper.writeValueAsString(message))
        writer.flush()
        Thread.sleep(100) // Simulate some processing time
    catch
      case e: Exception =>
        println(s"Error in stream: ${e.getMessage}")
    finally
      writer.close()

  override def doPost(request: HttpServletRequest, response: HttpServletResponse): Unit =
    val message = request.getReader().readLine()
    if message == null then
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST)
      response.getWriter.println("No message provided")
    else
      try
        val payload = Map(
          "type" -> "response",
          "content" -> s"Received: $message"
        )
        
        response.setContentType("application/json")
        response.setStatus(HttpServletResponse.SC_OK)
        response.getWriter.println(mapper.writeValueAsString(payload))
      catch
        case e: Exception =>
          response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR)
          response.getWriter.println(s"Error processing message: ${e.getMessage}")
