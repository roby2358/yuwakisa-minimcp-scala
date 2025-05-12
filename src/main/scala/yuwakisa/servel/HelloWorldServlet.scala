package yuwakisa.servel

import jakarta.servlet.http.{HttpServlet, HttpServletRequest, HttpServletResponse}

class HelloWorldServlet extends HttpServlet :

  override protected def doGet(request: HttpServletRequest, response: HttpServletResponse):Unit =
    println("Hello world servlet")
    Content.okText(response, "Hello world!")
