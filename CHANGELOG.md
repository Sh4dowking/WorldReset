# Changelog

All notable changes to WorldResetter will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Planned Features
- Configuration file support for customizable settings
- Multiple world profile support
- Backup creation before world reset
- Web dashboard for remote management
- Discord integration for notifications

## [1.0.0] - 2025-08-28

### Added
- **Core Functionality**
  - Complete world reset functionality for all dimensions (Overworld, Nether, End)
  - Automatic server restart with fresh world generation
  - Permission-based access control with `worldresetter.admin` permission
  - Confirmation requirement to prevent accidental resets

- **Architecture & Code Quality**
  - Professional modular architecture with separate manager classes
  - `ConfigManager` for server.properties and world configuration management
  - `WorldManager` for world operations and cleanup
  - `ScriptGenerator` for creating bash restart scripts
  - `ServerController` for server shutdown and restart operations
  - `LoggerUtil` for centralized, consistent logging

- **Safety & Reliability**
  - Process independence using `nohup` for script execution
  - Comprehensive error handling and fallback mechanisms
  - Graceful server shutdown with configurable timeouts
  - Automatic cleanup of old world directories from previous resets
  - Safe directory deletion with proper error handling

- **Logging & Monitoring**
  - Organized log files in `logs/` directory
  - Detailed operation logging with timestamps
  - Script execution output logging
  - Step-by-step process tracking
  - Success/failure indicators with visual symbols

- **World Management**
  - Unique world name generation using timestamps
  - Complete cache file cleanup (usercache.json, whitelist.json, etc.)
  - Cache directory cleanup (logs, versions, .paper-remapped)
  - Paper-specific cache cleanup
  - World data file pattern matching and cleanup

- **Documentation**
  - Comprehensive README.md with installation and usage instructions
  - Detailed API documentation with JavaDoc
  - Contributing guidelines for developers
  - Professional project structure and organization

### Technical Specifications
- **Supported Versions**: Bukkit/Spigot/Paper 1.21+
- **Java Version**: Java 17+
- **Dependencies**: Paper API 1.21.1-R0.1-SNAPSHOT
- **Build System**: Maven 3.6+
- **License**: MIT License

### Configuration
- **Server Directory**: `/home/ubuntu/MinecraftServer/TestingServer_Paper_1_21_8`
- **Memory Allocation**: 16GB (Xmx16G, Xms16G)
- **Screen Session**: `minecraft_minigames`
- **Process Timeouts**: Configurable delays for shutdown and cleanup

### Commands
- `/worldreset` - Display warning and instructions
- `/worldreset confirm` - Execute world reset process

### Permissions
- `worldresetter.admin` - Access to all world reset commands
- `worldresetter.*` - Grants all plugin permissions

---

## Version History Summary

- **v1.0.0** (2025-08-28): Initial professional release with complete functionality

## Upgrade Guide

### From Pre-1.0 Versions
This is the initial professional release. If upgrading from development versions:

1. Remove old plugin JAR files
2. Install WorldResetter-1.0.jar
3. Restart server
4. Verify permissions are correctly configured
5. Test functionality in a safe environment

## Breaking Changes

None for initial release.

## Support

For support with any version:
- Check the troubleshooting section in README.md
- Review the relevant version's documentation
- Submit issues on GitHub with version information

---

*Note: This project follows semantic versioning. Version numbers indicate the compatibility and scope of changes.*
