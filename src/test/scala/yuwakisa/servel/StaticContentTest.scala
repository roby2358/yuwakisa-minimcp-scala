package yuwakisa.servel

import munit.FunSuite
import java.io.ByteArrayOutputStream
import java.nio.charset.StandardCharsets

class StaticContentTest extends FunSuite:
  test("read content from test file"):
    val content = new StaticContent("/test/info.txt")
    val out = new ByteArrayOutputStream()
    content.stream(out)
    val result = new String(out.toByteArray, StandardCharsets.UTF_8)
    assertEquals(result, "banana\nboat\nbutton\n")

  test("handle directory paths by serving index.html"):
    val content = new StaticContent("/test/")
    assertEquals(content.resourcePath, "/public/test/index.html")

  test("detect correct js mime types"):
    val content = new StaticContent("/test/script.js")
    assertEquals(content.mimeType, "text/javascript")

  test("detect correct js mime types"):
    val content = new StaticContent("/test/script.js")
    assertEquals(content.mimeType, "text/javascript")

  test("detect correct js mime types"):
    val content = new StaticContent("/test/script.js")
    assertEquals(content.mimeType, "text/javascript")

  test("detect correct png mime types"):
    val content = new StaticContent("/test/image.png")
    assertEquals(content.mimeType, "image/png")

  test("detect correct not null mime types"):
    val txtContent = new StaticContent("/test/info.txt")
    assert(txtContent.mimeType != null)
    assert(txtContent.mimeType == "txt")

  test("handle non-existent resources"):
    val content = new StaticContent("/nonexistent.txt")
    assertEquals(content.resourceStream, None) 