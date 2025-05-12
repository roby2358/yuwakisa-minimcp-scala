package yuwakisa.servel
import java.io.{InputStream, OutputStream}
import java.nio.file.{Files, Paths}
import jakarta.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class StaticContentServlet extends HttpServlet :

  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse):Unit =
    val c = StaticContent(request.getRequestURI)

    Option(c.resourceStream).match
      case None => response.setStatus(HttpServletResponse.SC_NOT_FOUND)
      case Some(in) =>
        response.setStatus(HttpServletResponse.SC_OK)
        response.setContentType(c.mimeType)
//        println(s"path=${c.resourcePath} type=${c.mimeType}")
        c.stream(response.getOutputStream)
