# MiniMCP Server Documentation

## Overview
MiniMCP is a Scala-based MCP (Model Context Protocol) server implementation that provides a robust foundation for building AI model context management servers. The primary implementation uses stdio for communication, with an optional streamable server mode.

*This is just a toy, not a serious implementation!*

## Claude Desktop Integration

### Configuration
Claude Desktop under Settings -> Developer will help you find the configuration directory and `claude_deskstop_config.json`

Add the following to your Claude Desktop config:

```json
{
  "mcpServers": {
    "minimcp": {
      "command": "java",
      "args": ["--add-modules=jdk.unsupported", "-jar", "<path>/target/scala-3.3.6/minimcp.jar"]
    }
  }
}
```

Replace `<path>` with the absolute path to your MiniMCP project directory. For example, if your project is at `C:/work/minimcp`, the full path would be:
```json
{
  "mcpServers": {
    "minimcp": {
      "command": "java",
      "args": ["--add-modules=jdk.unsupported", "-jar", "C:/work/minimcp/target/scala-3.3.6/minimcp.jar"]
    }
  }
}
```

### Important Notes
1. The `--add-modules=jdk.unsupported` flag is required as the project uses some Java modules that need to be explicitly enabled
2. Make sure to use forward slashes (/) in the path, even on Windows
3. The JAR file will be created in the `target/scala-3.3.6/` directory after running `sbt assembly`
4. The server runs in stdio mode by default, which is what you want for Claude Desktop integration

### Using

Once you've enabled the MCP server, Claude will be able to see the tools and invoke them. You'll likely have to instruct it to do so, however.

Claude cannot see the resources or prompts.

Those are available for you to invoke manually under the "+" menu an selecting your MCP server.

Yeah, that's a hassle. Resources and prompts aren't very accessible. Mainly they're for you to add context in the prompt window.
