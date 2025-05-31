# MiniMCP Server Documentation

## Overview
MiniMCP is a Scala-based MCP (Model Context Protocol) server implementation that provides a robust foundation for building AI model context management servers. The primary implementation uses stdio for communication, with an optional streamable server mode. This server implementation focuses on protocol handling, packet processing, and context management while maintaining clean architecture and extensibility.

## Features
- Model Context Protocol implementation
- Primary stdio-based communication
- Optional streamable server mode
- Packet handling and processing
- Context management support
- Extensible architecture
- Scala-based implementation for type safety and functional programming benefits

## Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 8 or higher
- Scala Build Tool (sbt)
- Git

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/minimcp.git
   cd minimcp
   ```

2. Build the project:
   ```bash
   # Using sbt
   sbt assembly
   ```

3. Run the server:
   ```bash
   # Using the JAR file (production mode)
   # For stdio mode (default):
   java -jar target/scala-2.13/minimcp-assembly-0.1.0.jar

   # For streamable server mode:
   java -jar target/scala-2.13/minimcp-assembly-0.1.0.jar --streamable
   ```

   Note: The JAR file will be created in the `target/scala-2.13/` directory after running `sbt assembly`. The exact filename may vary depending on your project version.

## Development Guide

### Project Structure
```
minimcp/
├── src/
│   └── main/
│       └── scala/
│           └── yuwakisa/
│               ├── minimcp/    # Core MCP implementation
│               │   ├── stdio/  # Stdio protocol implementation
│               │   └── stream/ # Streamable server implementation
│               └── servel/     # Server implementation
├── project/                    # SBT project configuration
└── build.sbt                  # Build configuration
```

### Key Components
1. **Protocol Implementation**
   - Stdio-based communication (primary)
   - Streamable server mode (optional)
   - Handles Model Context Protocol version compatibility
   - Manages packet encoding/decoding
   - Implements protocol state machine

2. **Server Core**
   - Manages server lifecycle
   - Handles client connections
   - Implements context management

3. **Packet Processing**
   - Packet handlers for different protocol states
   - Event system for context events
   - Custom packet support

## Usage Examples

### Starting the Server
```scala
import yuwakisa.minimcp.Server

object Main {
  def main(args: Array[String]): Unit = {
    // For stdio mode (default)
    val server = new Server()
    server.start()

    // For streamable server mode
    val streamServer = new Server(streamable = true)
    streamServer.start()
  }
}
```

### Protocol Selection
The server supports two modes of operation:

1. **Stdio Mode (Default)**
   - Uses standard input/output for communication
   - Ideal for direct integration with other processes
   - Lower latency for single-client scenarios
   - Simple to integrate with existing systems

2. **Streamable Server Mode**
   - Enables network-based communication
   - Supports multiple concurrent clients
   - Useful for distributed deployments
   - Started with `--streamable` flag

### Adding Custom Packet Handlers
```scala
import yuwakisa.minimcp.packet.PacketHandler

class CustomPacketHandler extends PacketHandler {
  override def handle(packet: Packet): Unit = {
    // Custom packet handling logic
  }
}
```

## Contributing
1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License
This project is licensed under the terms specified in the LICENSE file.

## Support
For issues, feature requests, or questions, please open an issue in the GitHub repository.

## Best Practices for Claude
When working with this codebase, Claude should:

1. **Understand Protocol Context**
   - Be familiar with Model Context Protocol specifications
   - Understand stdio-based communication patterns
   - Consider streamable server requirements when needed
   - Understand packet structures and state transitions
   - Consider protocol version compatibility

2. **Code Style**
   - Follow Scala best practices
   - Use functional programming patterns where appropriate
   - Maintain type safety
   - Write clear documentation

3. **Testing**
   - Ensure protocol compatibility
   - Test packet handling
   - Verify context management
   - Maintain backward compatibility

4. **Performance Considerations**
   - Optimize packet processing
   - Manage memory efficiently
   - Handle concurrent connections properly

## Tools and Capabilities
The server provides several tools and capabilities:

1. **Protocol Tools**
   - Packet builders
   - Protocol state management
   - Version compatibility checks

2. **Server Tools**
   - Connection management
   - Context session handling
   - Context management

3. **Development Tools**
   - Debug logging
   - Performance monitoring
   - Testing utilities

## Common Tasks

### Adding New Packet Types
1. Define packet structure
2. Implement packet handler
3. Register handler with server
4. Update protocol documentation

### Implementing Context Management
1. Create context logic module
2. Implement event handlers
3. Register with server
4. Add configuration options

### Debugging
1. Enable debug logging
2. Use packet inspection tools
3. Monitor server performance
4. Check protocol compatibility

## Additional Resources
- Model Context Protocol Documentation
- Scala Documentation
- SBT Documentation
- Project Wiki (if available) 