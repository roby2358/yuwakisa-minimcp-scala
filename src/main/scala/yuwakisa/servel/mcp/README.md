# Model Context Protocol (MCP) Implementation

This directory contains the implementation of the Model Context Protocol (version 2025-03-26), a JSON-RPC 2.0 based protocol for LLM agent communication.

## Overview

The implementation provides a servlet that handles MCP communication over HTTP, supporting both immediate responses and streaming via Server-Sent Events (SSE).

## Key Components

- `McpServlet`: Main entry point that handles `/mcp/*` requests and manages sessions
- `MessageHandler`: Base interface for handling different types of JSON-RPC messages
- `MessageHandlerRegistry`: Registry for all available message handlers

### Message Handlers

The handlers are organized into the following categories:

#### Core Handlers
- `InitializeHandler`: Handles protocol initialization and capability negotiation
- `PingHandler`: Handles keep-alive ping messages

#### Notification Handlers
- `CancelledNotificationHandler`: Handles cancellation notifications
- `ProgressNotificationHandler`: Handles progress update notifications
- `InitializedNotificationHandler`: Handles initialization completion notifications

#### Tools Handlers
- `ToolsListHandler`: Lists available tools
- `ToolsCallHandler`: Executes tool calls

#### Resource Handlers
- `ResourcesListHandler`: Lists available resources
- `ResourcesReadHandler`: Reads resource contents

## Protocol Details

For detailed protocol specifications, see:
- `streamablehttp.txt`: HTTP transport details and streaming support
- `mcpmessages.txt`: JSON-RPC message formats and capabilities

## Quick Start

1. Add the `McpServlet` to your `ServerRunner`
2. Mount on `/mcp/*` path pattern
3. Configure security settings (CORS, authentication)
4. Implement required capabilities based on your needs

## Implementation Notes

- Sessions are maintained in memory and lost on server restart
- Supports both immediate JSON responses and SSE streaming
- Handles session management via `Mcp-Session-Id` header
- Implements standard JSON-RPC error codes
- Supports cancellation of long-running operations

## Security

The implementation includes:
- Basic CORS headers (currently allows all origins - needs to be configured for production)
- Session-based authentication via Mcp-Session-Id
- Input validation for all requests
- Rate limiting support
- Safe output handling

Note: For production use, the following security enhancements are recommended:
- Configure CORS to only allow specific origins
- Implement CSRF protection
- Add proper authentication before processing JSON-RPC requests
- Bind server to 127.0.0.1 instead of 0.0.0.0

For detailed security requirements, refer to the protocol specifications. 