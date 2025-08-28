package com.worldresetter.config;

/**
 * WorldResetter Plugin Constants
 * 
 * Contains all configuration constants and default values used throughout the plugin.
 * This centralized approach makes it easy to modify settings and maintain consistency.
 * 
 * @author Sh4dowking
 * @version 1.0.0
 * @since 1.0.0
 */
public final class Constants {
    
    // Plugin Information
    public static final String PLUGIN_NAME = "WorldResetter";
    public static final String PLUGIN_VERSION = "1.0.1";
    public static final String PLUGIN_PREFIX = "§7[§bWorldResetter§7]§r ";
    
    // Server Configuration
    public static final String SERVER_DIR = "/home/ubuntu/MinecraftServer/TestingServer_Paper_1_21_8";
    public static final String SERVER_PROPERTIES_FILE = "server.properties";
    public static final String RESTART_SCRIPT_NAME = "restart_server.sh";
    
    // World Configuration  
    public static final String[] DEFAULT_WORLD_NAMES = {"world", "world_nether", "world_the_end"};
    public static final String WORLD_NAME_PREFIX = "world_";
    public static final String NETHER_SUFFIX = "_nether";
    public static final String END_SUFFIX = "_the_end";
    
    // Logging Configuration
    public static final String LOGS_DIR = "logs";
    public static final String WORLD_RESET_LOG_PREFIX = "world_reset_";
    public static final String OUTPUT_LOG_NAME = "world_reset_output.log";
    
    // Timing Configuration (in seconds)
    public static final int SERVER_SHUTDOWN_DELAY = 3;
    public static final int PROCESS_CLEANUP_DELAY = 8;
    public static final int GRACEFUL_SHUTDOWN_TIMEOUT = 15;
    public static final int FORCE_KILL_DELAY = 3;
    public static final int VERIFICATION_DELAY = 3;
    
    // Server Memory Configuration
    public static final String JVM_MIN_MEMORY = "16G";
    public static final String JVM_MAX_MEMORY = "16G";
    public static final String SCREEN_SESSION_NAME = "minecraft_minigames";
    
    // Permissions
    public static final String ADMIN_PERMISSION = "worldresetter.admin";
    
    // Messages
    public static final class Messages {
        public static final String NO_PERMISSION = "§cYou don't have permission to use this command!";
        public static final String RESET_WARNING = "§c⚠ WARNING: This will delete ALL worlds and restart the server!";
        public static final String RESET_CONFIRMATION = "§cThis action cannot be undone! All player progress will be lost!";
        public static final String RESET_COMMAND_HINT = "§eType §f/worldreset confirm §eto proceed with the world reset.";
        public static final String RESET_INITIATED = "§c§l⚠ WORLD RESET INITIATED ⚠";
        public static final String RESET_BROADCAST = "§cAll worlds are being deleted and the server will restart!";
        public static final String REJOIN_MESSAGE = "§eYou can try rejoining in ~30 seconds...";
    }
    
    // File Patterns for Cleanup
    public static final String[] CACHE_FILES = {
        "usercache.json",
        "whitelist.json", 
        "banned-players.json",
        "banned-ips.json",
        "session.lock"
    };
    
    public static final String[] CACHE_DIRECTORIES = {
        "cache",
        "logs",
        "versions",
        ".paper-remapped"
    };
    
    public static final String[] WORLD_DATA_PATTERNS = {
        "level.dat*",
        "uid.dat"
    };
    
    // Legacy process patterns - no longer used (kept for compatibility)
    // The new implementation uses directory-specific process detection
    @Deprecated
    public static final String[] JAVA_PROCESS_PATTERNS = {
        "java.*paper.*jar",
        "java.*server.jar", 
        "minecraft"
    };
    
    // Private constructor to prevent instantiation
    private Constants() {
        throw new UnsupportedOperationException("Constants class cannot be instantiated");
    }
}
