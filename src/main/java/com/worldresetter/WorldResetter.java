package com.worldresetter;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import com.worldresetter.config.Constants;
import com.worldresetter.managers.ConfigManager;
import com.worldresetter.managers.ScriptGenerator;
import com.worldresetter.managers.ServerController;
import com.worldresetter.managers.WorldManager;
import com.worldresetter.utils.LoggerUtil;

/**
 * WorldResetter Plugin - Main Class
 * 
 * A professional Minecraft server plugin that provides safe and reliable world reset functionality.
 * This plugin allows server administrators to completely reset all worlds (Overworld, Nether, End)
 * and restart the server with fresh world generation.
 * 
 * Key Features:
 * - Complete world deletion and regeneration
 * - Automatic server restart with new world seeds
 * - Comprehensive cleanup of cache files and directories
 * - Safe process management with independent script execution
 * - Detailed logging and error handling
 * - Permission-based access control
 * 
 * Usage:
 * - /worldreset - Shows warning and requires confirmation
 * - /worldreset confirm - Executes the world reset process
 * 
 * @author Sh4dowking
 * @version 1.0.0
 * @since 1.0.0
 * @see <a href="https://github.com/Sh4dowking/WorldReset">GitHub Repository</a>
 */
public class WorldResetter extends JavaPlugin {
    
    // Manager instances for handling different aspects of the plugin
    private ConfigManager configManager;
    private WorldManager worldManager;
    private ScriptGenerator scriptGenerator;
    private ServerController serverController;
    
    /**
     * Called when the plugin is enabled.
     * Initializes all manager classes and validates the server environment.
     */
    @Override
    public void onEnable() {
        try {
            // Initialize logging utility
            LoggerUtil.initialize(this);
            
            LoggerUtil.logOperationStart("Plugin Initialization");
            LoggerUtil.info("Starting " + Constants.PLUGIN_NAME + " v" + Constants.PLUGIN_VERSION);
            
            // Initialize manager classes
            initializeManagers();
            
            // Validate server environment
            validateEnvironment();
            
            // Log successful initialization
            LoggerUtil.logOperationComplete("Plugin Initialization");
            LoggerUtil.logSuccess(Constants.PLUGIN_NAME + " plugin enabled successfully!");
            LoggerUtil.info("Use /worldreset to reset all worlds and restart the server");
            
            // Log environment information
            logEnvironmentInfo();
            
        } catch (Exception e) {
            LoggerUtil.severe("Critical error during plugin initialization", e);
            LoggerUtil.severe("Plugin will be disabled to prevent issues");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }
    
    /**
     * Called when the plugin is disabled.
     * Performs cleanup and logs shutdown information.
     */
    @Override
    public void onDisable() {
        LoggerUtil.info(Constants.PLUGIN_NAME + " plugin disabled");
        LoggerUtil.info("Thank you for using " + Constants.PLUGIN_NAME + "!");
    }
    
    /**
     * Handles command execution for the plugin.
     * Currently supports the /worldreset command with confirmation requirement.
     * 
     * @param sender The command sender
     * @param command The executed command
     * @param label The command label used
     * @param args The command arguments
     * @return true if the command was handled, false otherwise
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("worldreset")) {
            return false;
        }
        
        try {
            return handleWorldResetCommand(sender, args);
        } catch (Exception e) {
            LoggerUtil.severe("Error handling worldreset command", e);
            sender.sendMessage(Constants.PLUGIN_PREFIX + "§cAn internal error occurred. Check console for details.");
            return true;
        }
    }
    
    /**
     * Handles the worldreset command logic with permission checking and confirmation.
     * 
     * @param sender The command sender
     * @param args The command arguments
     * @return true if the command was handled successfully
     */
    private boolean handleWorldResetCommand(CommandSender sender, String[] args) {
        // Permission check
        if (!sender.hasPermission(Constants.ADMIN_PERMISSION)) {
            sender.sendMessage(Constants.PLUGIN_PREFIX + Constants.Messages.NO_PERMISSION);
            LoggerUtil.warning("User " + sender.getName() + " attempted worldreset without permission");
            return true;
        }
        
        // Show warning if no confirmation provided
        if (args.length == 0) {
            sendWorldResetWarning(sender);
            return true;
        }
        
        // Execute reset if confirmed
        if (args.length == 1 && args[0].equalsIgnoreCase("confirm")) {
            return executeWorldReset(sender);
        }
        
        // Invalid arguments
        sender.sendMessage(Constants.PLUGIN_PREFIX + "§cInvalid arguments. Use /worldreset or /worldreset confirm");
        return true;
    }
    
    /**
     * Sends world reset warning messages to the command sender.
     * 
     * @param sender The command sender to send warnings to
     */
    private void sendWorldResetWarning(CommandSender sender) {
        sender.sendMessage(Constants.PLUGIN_PREFIX + Constants.Messages.RESET_WARNING);
        sender.sendMessage(Constants.PLUGIN_PREFIX + Constants.Messages.RESET_CONFIRMATION);
        sender.sendMessage(Constants.PLUGIN_PREFIX + Constants.Messages.RESET_COMMAND_HINT);
        
        LoggerUtil.info("World reset warning displayed to " + sender.getName());
    }
    
    /**
     * Executes the world reset process after confirmation.
     * 
     * @param sender The command sender who initiated the reset
     * @return true if the reset process was initiated successfully
     */
    private boolean executeWorldReset(CommandSender sender) {
        LoggerUtil.logOperationStart("World Reset Execution");
        LoggerUtil.info("World reset initiated by: " + sender.getName());
        
        // Broadcast reset messages to all players
        broadcastResetMessages();
        
        // Get current level name for cleanup
        String currentLevelName = configManager.getCurrentLevelName();
        LoggerUtil.info("Current level name: " + currentLevelName);
        
        // Update world configuration with new seed and level name
        if (!configManager.updateWorldConfiguration()) {
            sender.sendMessage(Constants.PLUGIN_PREFIX + "§cFailed to update world configuration!");
            LoggerUtil.severe("Failed to update world configuration during reset");
            return true;
        }
        
        // Execute the world reset process
        if (!serverController.executeWorldReset(currentLevelName)) {
            sender.sendMessage(Constants.PLUGIN_PREFIX + "§cFailed to execute world reset!");
            LoggerUtil.severe("Failed to execute world reset process");
            return true;
        }
        
        LoggerUtil.logSuccess("World reset process initiated successfully");
        return true;
    }
    
    /**
     * Broadcasts world reset messages to all online players.
     */
    private void broadcastResetMessages() {
        Bukkit.broadcastMessage(Constants.PLUGIN_PREFIX + Constants.Messages.RESET_INITIATED);
        Bukkit.broadcastMessage(Constants.PLUGIN_PREFIX + Constants.Messages.RESET_BROADCAST);
        Bukkit.broadcastMessage(Constants.PLUGIN_PREFIX + Constants.Messages.REJOIN_MESSAGE);
        
        LoggerUtil.info("World reset messages broadcasted to all players");
    }
    
    /**
     * Initializes all manager classes required by the plugin.
     * 
     * @throws IllegalStateException if any manager fails to initialize
     */
    private void initializeManagers() {
        LoggerUtil.info("Initializing plugin managers...");
        
        try {
            // Initialize configuration manager
            configManager = new ConfigManager();
            LoggerUtil.debug("ConfigManager initialized");
            
            // Initialize world manager  
            worldManager = new WorldManager(configManager.getServerDirectory());
            LoggerUtil.debug("WorldManager initialized");
            
            // Initialize script generator
            scriptGenerator = new ScriptGenerator(configManager.getServerDirectory(), worldManager);
            LoggerUtil.debug("ScriptGenerator initialized");
            
            // Initialize server controller
            serverController = new ServerController(configManager.getServerDirectory(), scriptGenerator);
            LoggerUtil.debug("ServerController initialized");
            
            LoggerUtil.logSuccess("All managers initialized successfully");
            
        } catch (Exception e) {
            throw new IllegalStateException("Failed to initialize plugin managers", e);
        }
    }
    
    /**
     * Validates the server environment for world reset operations.
     * 
     * @throws IllegalStateException if the environment is invalid
     */
    private void validateEnvironment() {
        LoggerUtil.info("Validating server environment...");
        
        if (!serverController.validateServerEnvironment()) {
            throw new IllegalStateException("Server environment validation failed");
        }
        
        LoggerUtil.logSuccess("Server environment validated successfully");
    }
    
    /**
     * Logs detailed information about the server environment.
     * This helps with debugging and provides useful context.
     */
    private void logEnvironmentInfo() {
        try {
            // Log server controller environment info
            ServerController.ServerEnvironmentInfo envInfo = serverController.getEnvironmentInfo();
            LoggerUtil.info("Server Environment: " + envInfo.toString());
            
            // Log world statistics
            WorldManager.WorldStats worldStats = worldManager.getWorldStats();
            LoggerUtil.info("World Statistics: " + worldStats.toString());
            
            // Log current level name
            String currentLevel = configManager.getCurrentLevelName();
            LoggerUtil.info("Current Level Name: " + currentLevel);
            
        } catch (Exception e) {
            LoggerUtil.warning("Could not gather complete environment information: " + e.getMessage());
        }
    }
}
