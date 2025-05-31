package yuwakisa.servel.mcp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.util.{Try, Success, Failure}
import McpMessageTypes.*

class StdioTransport:
  private val objectMapper = new ObjectMapper().registerModule(DefaultScalaModule)
  private val reader = new BufferedReader(new InputStreamReader(System.in))
  private val writer = new PrintWriter(System.out, true)
  private val errorWriter = new PrintWriter(System.err, true)

  def readMessage(): Try[JsonRpcRequest] =
    Try:
      val line = reader.readLine()
      println(line)
      if line == null then
        throw new IllegalStateException("End of input stream")
      objectMapper.readValue(line, classOf[JsonRpcRequest])

  def writeMessage(message: JsonRpcMessage): Unit =
    val json = objectMapper.writeValueAsString(message)
    writer.println(json)
    writer.flush()

  def writeError(message: String): Unit =
    errorWriter.println(s"Error: $message")
    errorWriter.flush()

  def close(): Unit =
    reader.close()
    writer.close()
    errorWriter.close() 