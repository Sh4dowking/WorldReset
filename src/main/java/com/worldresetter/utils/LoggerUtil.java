package com.worldresetter.utils;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Centralized logging utility for the WorldResetter plugin.
 * 
 * Provides consistent logging functionality with proper formatting and level management.
 * All plugin logging should go through this utility to ensure consistency and maintainability.
 * 
 * @author Sh4dowking
 * @version 1.0.0
 * @since 1.0.0
 */
public final class LoggerUtil {
    
    private static JavaPlugin plugin;
    
    /**
     * Initializes the logger utility with the plugin instance.
     * This method must be called during plugin initialization.
     * 
     * @param pluginInstance The main plugin instance
     * @throws IllegalArgumentException if pluginInstance is null
     */
    public static void initialize(JavaPlugin pluginInstance) {
        if (pluginInstance == null) {
            throw new IllegalArgumentException("Plugin instance cannot be null");
        }
        plugin = pluginInstance;
    }
    
    /**
     * Logs an informational message.
     * 
     * @param message The message to log
     */
    public static void info(String message) {
        if (plugin != null) {
            plugin.getLogger().info(message);
        }
    }
    
    /**
     * Logs a warning message.
     * 
     * @param message The warning message to log
     */
    public static void warning(String message) {
        if (plugin != null) {
            plugin.getLogger().warning(message);
        }
    }
    
    /**
     * Logs a severe error message.
     * 
     * @param message The error message to log
     */
    public static void severe(String message) {
        if (plugin != null) {
            plugin.getLogger().severe(message);
        }
    }
    
    /**
     * Logs a severe error message with exception details.
     * 
     * @param message The error message to log
     * @param throwable The exception that occurred
     */
    public static void severe(String message, Throwable throwable) {
        if (plugin != null) {
            plugin.getLogger().severe(message);
            if (throwable != null) {
                plugin.getLogger().severe("Exception details: " + throwable.getMessage());
                throwable.printStackTrace();
            }
        }
    }
    
    /**
     * Logs a debug message (only in development mode).
     * 
     * @param message The debug message to log
     */
    public static void debug(String message) {
        if (plugin != null && isDebugMode()) {
            plugin.getLogger().info("[DEBUG] " + message);
        }
    }
    
    /**
     * Logs the start of a major operation.
     * 
     * @param operation The operation being started
     */
    public static void logOperationStart(String operation) {
        info("=== " + operation.toUpperCase() + " STARTED ===");
    }
    
    /**
     * Logs the completion of a major operation.
     * 
     * @param operation The operation that completed
     */
    public static void logOperationComplete(String operation) {
        info("=== " + operation.toUpperCase() + " COMPLETED ===");
    }
    
    /**
     * Logs a step in a multi-step process.
     * 
     * @param stepNumber The step number
     * @param totalSteps The total number of steps
     * @param stepDescription Description of the current step
     */
    public static void logStep(int stepNumber, int totalSteps, String stepDescription) {
        info(String.format("Step %d/%d: %s", stepNumber, totalSteps, stepDescription));
    }
    
    /**
     * Logs a successful operation with a checkmark.
     * 
     * @param message The success message
     */
    public static void logSuccess(String message) {
        info("✓ " + message);
    }
    
    /**
     * Logs a failed operation with an X mark.
     * 
     * @param message The failure message
     */
    public static void logFailure(String message) {
        warning("✗ " + message);
    }
    
    /**
     * Checks if debug mode is enabled.
     * 
     * @return true if debug mode is enabled
     */
    private static boolean isDebugMode() {
        // This could be made configurable in the future
        return false;
    }
    
    // Private constructor to prevent instantiation
    private LoggerUtil() {
        throw new UnsupportedOperationException("LoggerUtil is a utility class and cannot be instantiated");
    }
}
