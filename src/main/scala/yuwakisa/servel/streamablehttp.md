Streamable HTTP Transport — Model Context Protocol (2025-03-26)
A terse, implementation-accurate reference for LLM agents.

### 1. Core idea

All MCP traffic is JSON-RPC 2.0 over one HTTP path (e.g. /mcp). The server upgrades any response to Server-Sent Events (SSE) only when streaming is required. ([Model Context Protocol][1], [The Cloudflare Blog][2])

2 Endpoint contract
Aspect	Requirement
URL	One server-advertised path (e.g. /mcp).
Methods	POST — send messages; GET — open/maintain SSE stream; optional DELETE — terminate session.
Client headers	POST Accept: application/json, text/event-stream and Content-Type: application/json; after init include Mcp-Session-Id.
GET Accept: text/event-stream (+ Mcp-Session-Id).
Server headers	Content-Type: application/json or text/event-stream; echoes Mcp-Session-Id after it is assigned.

### 2. Endpoint contract

| Aspect           | Requirement                                                                            |
| ---------------- | -------------------------------------------------------------------------------------- |
| URL              | Any path; server advertises exactly one.                                               |
| Methods          | `POST` (send), `GET` (listen), optional `DELETE` (end session).                        |
| Headers (client) | `Accept: application/json, text/event-stream`; add `Mcp-Session-Id` once issued.       |
| Headers (server) | `Content-Type: application/json` **or** `text/event-stream`; may set `Mcp-Session-Id`. |

---

### 3. Client → Server (`POST`)

1. Body is a single JSON-RPC object or batch array.
2. If **no requests** (only notifications/responses) → server returns **202 Accepted** with empty body.
3. If **any requests** → server replies:

   * **Immediate**: one JSON object (`application/json`).
   * **Streaming**: starts an SSE stream (`text/event-stream`). Each `data:` line contains a JSON-RPC message; finish with the final response(s). ([Model Context Protocol][1])

Cancel: send a `CancelledNotification` in a new `POST`.

---

### 4. Server → Client

*Passive stream* – client sends `GET` + `Accept: text/event-stream`.
*Active stream* – server upgrades the response of the original `POST`.
Server may interleave its own **notifications or requests** before the eventual response. Either side may close the stream at any time. ([Model Context Protocol][1])

---

### 5. Resumability

* Servers may assign `id:` fields to SSE events.
* Client reconnects with `Last-Event-ID` to replay missed data. Stream IDs are per-stream, not global. ([Model Context Protocol][1])

---

### 6. Session management (optional)

* Server returns `Mcp-Session-Id` header on the `InitializeResult`.
* Client echoes it on every later request.
* Server can kill the session (future requests → **404**).
* Client can send `DELETE /mcp` with the header to end the session. ([Model Context Protocol][1])

---

### 7. Security checklist

* Verify `Origin` on **every** request.
* Bind local servers to `127.0.0.1`, not `0.0.0.0`.
* Require auth before processing. ([Model Context Protocol][1])

---

### 8. Status codes quick reference

| Code | Meaning                                                        |
| ---- | -------------------------------------------------------------- |
| 200  | Immediate JSON response.                                       |
| 202  | Accepted notifications/responses, no body.                     |
| 400  | Malformed JSON-RPC or missing session header.                  |
| 404  | Dead session.                                                  |
| 405  | Method not allowed (e.g., `GET` on servers that don’t stream). |

---

### 9. Minimal example (curl)

```bash
# 1. Start session + tool call
curl -N -X POST https://toolbox.local/mcp \
  -H "Accept: application/json, text/event-stream" \
  -d '{ "jsonrpc":"2.0","id":1,"method":"doThing","params":{} }'

# server may reply immediately OR stream:
# Content-Type: text/event-stream
# data: {"jsonrpc":"2.0","id":1,"result":"ok"}
```

```bash
# 2. Passive listening channel (optional)
curl -N -H "Accept: text/event-stream" https://toolbox.local/mcp
```

```bash
# 3. Cancel a long job
curl -X POST https://toolbox.local/mcp \
  -H "Mcp-Session-Id: abc123" \
  -d '{ "jsonrpc":"2.0","method":"CancelledNotification","params":{"id":1} }'
```

---

**That’s the whole transport:** one endpoint, POST for talks, SSE for streams, optional GET for idle listening, plus a session header when needed. Implement these rules and your agent will speak fluent MCP Streamable HTTP.

[1]: https://modelcontextprotocol.io/specification/2025-03-26/basic/transports "Transports - Model Context Protocol"
[2]: https://blog.cloudflare.com/streamable-http-mcp-servers-python/ "Bringing streamable HTTP transport and Python language support to MCP servers"
