# MCP Protocol Documentation

## Endpoints

### Health Check
`health` (Request → Response)

Tests the availability of the MCP server.

#### Request Parameters
None

#### Response (result object)
| Field | Type | Notes |
|-------|------|-------|
| protocolVersion | string | Version that the server will use (2.0) |

#### Error Codes
- `-32602`: Invalid params — unknown capability key or missing mandatory field
- `-32001`: Incompatible protocol version

#### Example Response
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    }
  }
}
```

### Initialize
`initialize` (Request → Response)

Handshake kicked off by the client to negotiate protocol revision and advertise capabilities.

#### Request Parameters
| Field | Type | Required | Notes |
|-------|------|----------|-------|
| jsonrpc | "2.0" | ✔️ | JSON-RPC version string |
| id | string \| number | ✔️ | Correlates request/response |
| method | "initialize" | ✔️ | |
| params | object | ✔️ | See sub-fields below |
| ├─ protocolVersion | string | ✔️ | Semantic date, e.g. "2024-11-05" |
| ├─ clientInfo | object | ✔️ | name, version |
| └─ capabilities | object \| null | – | Arbitrary feature flags (e.g. "sampling") |

#### Response (result object)
| Field | Type | Notes |
|-------|------|-------|
| protocolVersion | string | Version that the server will use |
| serverInfo | object | name, version, and capabilities object |

#### Error Codes
- `-32602`: Invalid params — unknown capability key or missing mandatory field
- `-32001`: Incompatible protocol version

#### Example Request
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "method": "initialize",
  "params": {
    "protocolVersion": "2024-11-05",
    "clientInfo": { 
      "name": "AcmeCLI", 
      "version": "1.2.3" 
    },
    "capabilities": { 
      "sampling": {} 
    }
  }
}
```

#### Example Response
```json
{
  "jsonrpc": "2.0",
  "id": 1,
  "result": {
    "protocolVersion": "2024-11-05",
    "serverInfo": {
      "name": "MCP-Server",
      "version": "0.9.0",
      "capabilities": {
        "tools": {},
        "resources": { 
          "subscribe": true, 
          "listChanged": true 
        },
        "prompts": {},
        "logging": {}
      }
    }
  }
}
```
