package com.worldresetter.managers;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;

import com.worldresetter.config.Constants;
import com.worldresetter.utils.LoggerUtil;

/**
 * Controls server operations including shutdown and restart processes.
 * 
 * This class manages the execution of restart scripts and handles the proper
 * sequencing of world reset operations. It ensures that scripts run independently
 * of the Java process to prevent premature termination.
 * 
 * @author Sh4dowking
 * @version 1.0.0
 * @since 1.0.0
 */
public class ServerController {
    
    private final File serverDirectory;
    private final ScriptGenerator scriptGenerator;
    
    /**
     * Constructs a new ServerController instance.
     * 
     * @param serverDirectory The server directory for operations
     * @param scriptGenerator The script generator for creating restart scripts
     * @throws IllegalArgumentException if parameters are null or invalid
     */
    public ServerController(File serverDirectory, ScriptGenerator scriptGenerator) {
        if (serverDirectory == null || !serverDirectory.exists()) {
            throw new IllegalArgumentException("Server directory must exist");
        }
        if (scriptGenerator == null) {
            throw new IllegalArgumentException("ScriptGenerator cannot be null");
        }
        
        this.serverDirectory = serverDirectory;
        this.scriptGenerator = scriptGenerator;
        
        LoggerUtil.debug("ServerController initialized for directory: " + serverDirectory.getAbsolutePath());
    }
    
    /**
     * Executes the complete world reset and server restart process.
     * This method coordinates all the necessary steps in the correct order.
     * 
     * @param currentLevelName The current level name from server.properties
     * @return true if the process was initiated successfully, false otherwise
     */
    public boolean executeWorldReset(String currentLevelName) {
        try {
            LoggerUtil.logOperationStart("World Reset Process");
            
            // Step 1: Generate the restart script
            LoggerUtil.logStep(1, 3, "Generating restart script...");
            if (!scriptGenerator.generateRestartScript(currentLevelName)) {
                LoggerUtil.logFailure("Failed to generate restart script");
                return false;
            }
            
            // Step 2: Execute the restart script independently  
            LoggerUtil.logStep(2, 3, "Executing restart script independently...");
            if (!executeRestartScript()) {
                LoggerUtil.logFailure("Failed to execute restart script");
                return false;
            }
            
            // Step 3: Shutdown server to allow script to run
            LoggerUtil.logStep(3, 3, "Shutting down server to complete reset...");
            scheduleServerShutdown();
            
            LoggerUtil.logSuccess("World reset process initiated successfully");
            return true;
            
        } catch (Exception e) {
            LoggerUtil.severe("Critical error during world reset process", e);
            
            // Emergency fallback - shutdown server anyway
            LoggerUtil.warning("Attempting emergency shutdown without script...");
            Bukkit.shutdown();
            return false;
        }
    }
    
    /**
     * Executes the restart script with proper process detachment.
     * Uses nohup to ensure the script continues running after the Java process exits.
     * 
     * @return true if the script execution was started successfully
     */
    private boolean executeRestartScript() {
        try {
            // Ensure logs directory exists for output redirection
            File logsDir = new File(serverDirectory, Constants.LOGS_DIR);
            if (!logsDir.exists() && !logsDir.mkdirs()) {
                LoggerUtil.warning("Could not create logs directory: " + logsDir.getAbsolutePath());
            }
            
            // Create process builder with nohup for independence
            ProcessBuilder processBuilder = new ProcessBuilder("nohup", "/bin/bash", "./" + Constants.RESTART_SCRIPT_NAME);
            processBuilder.directory(serverDirectory);
            
            // Redirect output to logs folder to prevent process hanging
            File outputFile = new File(logsDir, Constants.OUTPUT_LOG_NAME);
            processBuilder.redirectOutput(outputFile);
            processBuilder.redirectError(outputFile);
            
            LoggerUtil.info("Starting restart script process with nohup...");
            LoggerUtil.debug("Script path: " + scriptGenerator.getRestartScriptFile().getAbsolutePath());
            LoggerUtil.debug("Output log: " + outputFile.getAbsolutePath());
            
            // Start the process
            Process process = processBuilder.start();
            
            LoggerUtil.logSuccess("Restart script started independently (PID may not be accessible)");
            LoggerUtil.info("Script output will be logged to: " + outputFile.getAbsolutePath());
            
            return true;
            
        } catch (IOException e) {
            LoggerUtil.severe("Failed to execute restart script", e);
            return false;
        }
    }
    
    /**
     * Schedules a server shutdown with a brief delay.
     * This allows the restart script to start before the server terminates.
     */
    private void scheduleServerShutdown() {
        Bukkit.getScheduler().runTaskLater(
            getPluginInstance(),
            () -> {
                LoggerUtil.info("Executing scheduled server shutdown for world reset...");
                Bukkit.shutdown();
            },
            Constants.SERVER_SHUTDOWN_DELAY * 20L // Convert seconds to ticks (20 ticks per second)
        );
        
        LoggerUtil.info("Server shutdown scheduled in " + Constants.SERVER_SHUTDOWN_DELAY + " seconds");
    }
    
    /**
     * Gets the plugin instance for scheduling tasks.
     * This uses reflection to access the plugin instance since we don't have direct access.
     * 
     * @return The plugin instance
     */
    private org.bukkit.plugin.Plugin getPluginInstance() {
        // Get the plugin instance from Bukkit's plugin manager
        return Bukkit.getPluginManager().getPlugin(Constants.PLUGIN_NAME);
    }
    
    /**
     * Validates that the server environment is ready for world reset operations.
     * This checks for required files and permissions.
     * 
     * @return true if the environment is valid, false otherwise
     */
    public boolean validateServerEnvironment() {
        try {
            // Check server directory
            if (!serverDirectory.exists() || !serverDirectory.isDirectory()) {
                LoggerUtil.severe("Server directory does not exist: " + serverDirectory.getAbsolutePath());
                return false;
            }
            
            if (!serverDirectory.canWrite()) {
                LoggerUtil.severe("Cannot write to server directory: " + serverDirectory.getAbsolutePath());
                return false;
            }
            
            // Check for server.properties
            File propertiesFile = new File(serverDirectory, Constants.SERVER_PROPERTIES_FILE);
            if (!propertiesFile.exists()) {
                LoggerUtil.severe("Server properties file not found: " + propertiesFile.getAbsolutePath());
                return false;
            }
            
            // Check for server jar
            boolean jarFound = false;
            File[] files = serverDirectory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.getName().matches(".*\\.jar$")) {
                        jarFound = true;
                        LoggerUtil.debug("Found server jar: " + file.getName());
                        break;
                    }
                }
            }
            
            if (!jarFound) {
                LoggerUtil.warning("No server jar files found in server directory");
                // This is a warning, not a failure, as the script will check for jars
            }
            
            LoggerUtil.debug("Server environment validation completed successfully");
            return true;
            
        } catch (SecurityException e) {
            LoggerUtil.severe("Security exception during environment validation", e);
            return false;
        }
    }
    
    /**
     * Gets information about the current server environment.
     * This is useful for debugging and logging purposes.
     * 
     * @return A ServerEnvironmentInfo object with environment details
     */
    public ServerEnvironmentInfo getEnvironmentInfo() {
        File[] files = serverDirectory.listFiles();
        int worldDirectories = 0;
        int jarFiles = 0;
        long totalSize = 0;
        
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory() && (file.getName().startsWith("world") || file.getName().contains("world"))) {
                    worldDirectories++;
                }
                if (file.getName().endsWith(".jar")) {
                    jarFiles++;
                }
                totalSize += file.length();
            }
        }
        
        return new ServerEnvironmentInfo(
            serverDirectory.getAbsolutePath(),
            files != null ? files.length : 0,
            worldDirectories,
            jarFiles,
            totalSize
        );
    }
    
    /**
     * Inner class to hold server environment information.
     */
    public static class ServerEnvironmentInfo {
        private final String serverPath;
        private final int totalFiles;
        private final int worldDirectories;
        private final int jarFiles;
        private final long totalSize;
        
        public ServerEnvironmentInfo(String serverPath, int totalFiles, int worldDirectories, int jarFiles, long totalSize) {
            this.serverPath = serverPath;
            this.totalFiles = totalFiles;
            this.worldDirectories = worldDirectories;
            this.jarFiles = jarFiles;
            this.totalSize = totalSize;
        }
        
        public String getServerPath() { return serverPath; }
        public int getTotalFiles() { return totalFiles; }
        public int getWorldDirectories() { return worldDirectories; }
        public int getJarFiles() { return jarFiles; }
        public long getTotalSize() { return totalSize; }
        
        @Override
        public String toString() {
            return String.format("ServerEnvironment{path='%s', files=%d, worlds=%d, jars=%d, size=%dMB}", 
                               serverPath, totalFiles, worldDirectories, jarFiles, totalSize / 1024 / 1024);
        }
    }
}
