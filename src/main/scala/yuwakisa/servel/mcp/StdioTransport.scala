package yuwakisa.servel.mcp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.{BufferedReader, InputStreamReader, PrintWriter, IOException}
import scala.util.{Try, Success, Failure}
import McpMessageTypes.*

class StdioTransport:
  private val objectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
  private val reader = new BufferedReader(new InputStreamReader(System.in))
  private val writer = new PrintWriter(System.out, true)
  private val errorWriter = new PrintWriter(System.err, true)

  private def trace(message: String): Unit =
    errorWriter.println(s"[TRACE] $message")
    errorWriter.flush()

  def readMessage(): Try[JsonRpcRequest] =
    Try:
      trace("Reading message from stdin")
      val line = reader.readLine()
      if line == null then
        trace("Received null line - end of input stream")
        throw new IllegalStateException("End of input stream")
      trace(s"Read line: $line")
      objectMapper.readValue(line, classOf[JsonRpcRequest])

  def writeMessage(message: JsonRpcMessage): Try[Unit] =
    Try:
      trace("Writing message")
      val json = objectMapper.writeValueAsString(message)
      trace(s"Writing message to stdout: $json")
      writer.println(json)
      writer.flush()
    .recoverWith { case e: IOException =>
      trace(s"Failed to write message: ${e.getMessage}")
      Failure(new IOException(s"Failed to write message: ${e.getMessage}", e))
    }

  def writeError(message: String): Try[Unit] =
    Try:
      trace(s"Writing error: $message")
      errorWriter.println(s"Error: $message")
      errorWriter.flush()
    .recoverWith { case e: IOException =>
      trace(s"Failed to write error: ${e.getMessage}")
      Failure(new IOException(s"Failed to write error: ${e.getMessage}", e))
    }

  def close(): Unit =
    trace("Closing transport")
    Try(reader.close())
    Try(writer.close())
    Try(errorWriter.close()) 