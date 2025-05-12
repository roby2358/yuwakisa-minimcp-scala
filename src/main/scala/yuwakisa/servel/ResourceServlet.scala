package yuwakisa.servel

import jakarta.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class ResourceServlet extends HttpServlet :

  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse):Unit =
    Content.okJson(response, {"hello" -> "world"})
