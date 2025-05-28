package yuwakisa.servel.mcp

enum ResourceContent:
  case Text(text: String)
  case Blob(data: String, mimeType: String) 