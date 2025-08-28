# WorldResetter Plugin

A professional Minecraft server plugin that provides safe and reliable world reset functionality for Bukkit/Spigot/Paper servers.

## Overview

WorldResetter allows server administrators to completely reset all worlds (Overworld, Nether, End) and restart the server with fresh world generation. The plugin is designed with safety, reliability, and ease of use in mind.

## Features

- 🌍 **Complete World Reset**: Safely deletes and regenerates all world dimensions
- 🔄 **Automatic Server Restart**: Seamlessly restarts the server after world deletion  
- 🧹 **Comprehensive Cleanup**: Removes cache files, logs, and leftover directories
- 🔒 **Permission-Based Access**: Secure command access with permission nodes
- 📝 **Detailed Logging**: Comprehensive logging with organized log files
- ⚙️ **Process Independence**: Scripts run independently to prevent process termination issues
- 🎯 **Professional Architecture**: Clean, modular code structure ready for production

## Installation

1. Download the `WorldResetter-1.0.jar` file
2. Place it in your server's `plugins/` directory
3. Restart your server
4. Grant permissions to administrators (see [Permissions](#permissions))

## Usage

### Commands

| Command | Description | Permission |
|---------|-------------|------------|
| `/worldreset` | Shows warning and instructions | `worldresetter.admin` |
| `/worldreset confirm` | Executes the world reset process | `worldresetter.admin` |

### Basic Workflow

1. **Warning Phase**: Run `/worldreset` to see the warning message
2. **Confirmation**: Run `/worldreset confirm` to proceed with the reset
3. **Automatic Process**: The plugin handles everything automatically:
   - Broadcasts warning to all players
   - Updates world configuration with new seed
   - Creates and executes restart script
   - Shuts down server for world deletion
   - Removes all world files and cache
   - Restarts server with fresh worlds

## Permissions

| Permission | Description | Default |
|------------|-------------|---------|
| `worldresetter.admin` | Access to all world reset commands | `op` |

### Permission Setup Examples

**LuckPerms:**
```
/lp user <username> permission set worldresetter.admin true
/lp group admin permission set worldresetter.admin true
```

**GroupManager:**
```
/mangaddp <username> worldresetter.admin
/mangaddp <groupname> worldresetter.admin
```

## Configuration

The plugin automatically configures itself based on your server environment. Key settings are defined in the `Constants` class:

- **Server Directory**: `/home/ubuntu/MinecraftServer/TestingServer_Paper_1_21_8`
- **Memory Allocation**: 16GB (Xmx16G, Xms16G)
- **Screen Session**: `minecraft_minigames`
- **Log Directory**: `logs/`

## Architecture

### Project Structure

```
src/main/java/com/worldresetter/
├── WorldResetter.java          # Main plugin class
├── config/
│   └── Constants.java          # Configuration constants
├── managers/
│   ├── ConfigManager.java      # Server properties management
│   ├── WorldManager.java       # World operations
│   ├── ScriptGenerator.java    # Script generation
│   └── ServerController.java   # Server control operations
└── utils/
    └── LoggerUtil.java        # Centralized logging utility
```

### Key Components

- **ConfigManager**: Handles server.properties and world configuration
- **WorldManager**: Manages world directories and cleanup operations
- **ScriptGenerator**: Creates bash scripts for server restart
- **ServerController**: Controls server shutdown and restart processes
- **LoggerUtil**: Provides consistent logging throughout the plugin

## Technical Details

### World Reset Process

1. **Validation**: Checks server environment and permissions
2. **Configuration Update**: Generates new world seed and unique level name
3. **Script Generation**: Creates comprehensive bash restart script
4. **Process Execution**: Launches script with `nohup` for independence
5. **Server Shutdown**: Gracefully shuts down server
6. **World Deletion**: Script removes all world files and cache
7. **Server Restart**: Starts server in screen session with fresh worlds

### Safety Features

- **Permission Checks**: Ensures only authorized users can reset worlds
- **Confirmation Required**: Prevents accidental world resets
- **Process Independence**: Scripts run independently of Java process
- **Comprehensive Logging**: All operations are logged for debugging
- **Error Handling**: Graceful error handling with fallback mechanisms
- **Cache Cleanup**: Removes all world-related cache and temporary files

### Supported Server Types

- ✅ Bukkit 1.21+
- ✅ Spigot 1.21+  
- ✅ Paper 1.21+ (Recommended)
- ✅ Other Bukkit-compatible servers

## Logging

### Log Files

All logs are organized in the `logs/` directory:

- `world_reset_YYYYMMDD_HHMMSS.log` - Detailed reset process logs
- `world_reset_output.log` - Script execution output
- Server console logs contain plugin status and debug information

### Log Levels

- **INFO**: General operational information
- **WARNING**: Non-critical issues that should be noted
- **SEVERE**: Critical errors that require attention
- **DEBUG**: Detailed debugging information (disabled by default)

## Troubleshooting

### Common Issues

**Issue**: World reset command doesn't work
- **Solution**: Check that you have the `worldresetter.admin` permission
- **Solution**: Verify the server environment is supported

**Issue**: Server doesn't restart after reset
- **Solution**: Check the script logs in `logs/world_reset_output.log`
- **Solution**: Verify the server jar file exists and is accessible

**Issue**: Old world directories remain
- **Solution**: The plugin automatically cleans up old worlds from previous resets
- **Solution**: Check the cleanup section in the reset logs

### Getting Help

1. Check the server console for error messages
2. Review log files in the `logs/` directory
3. Verify your server environment meets the requirements
4. Check permissions are correctly configured

## Development

### Building from Source

1. Clone the repository
2. Ensure you have Maven installed
3. Run `mvn clean package`
4. The compiled JAR will be in the `target/` directory

### Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes following the existing code style
4. Add tests for new functionality
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Changelog

### Version 1.0.0
- Initial release
- Complete world reset functionality
- Modular architecture with manager classes
- Comprehensive logging system
- Professional documentation
- Organized log file structure

## Support

For support, bug reports, or feature requests, please:
1. Check the troubleshooting section
2. Review existing issues on [GitHub](https://github.com/Sh4dowking/WorldReset/issues)
3. Create a new issue with detailed information
4. Include relevant log files and server information

---

**WorldResetter** - Professional world management for Minecraft servers.

*Repository: https://github.com/Sh4dowking/WorldReset*
