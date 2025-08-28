package com.worldresetter.managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Random;

import com.worldresetter.config.Constants;
import com.worldresetter.utils.LoggerUtil;

/**
 * Manages configuration files and server properties for the WorldResetter plugin.
 * 
 * This class handles reading and writing server.properties, generating new world seeds,
 * and managing world names. It ensures that world resets use unique identifiers to
 * prevent conflicts and enable proper cleanup.
 * 
 * @author Sh4dowking
 * @version 1.0.0
 * @since 1.0.0
 */
public class ConfigManager {
    
    private final File serverDirectory;
    private final File propertiesFile;
    
    /**
     * Constructs a new ConfigManager instance.
     * 
     * @throws IllegalStateException if the server directory or properties file doesn't exist
     */
    public ConfigManager() {
        this.serverDirectory = new File(Constants.SERVER_DIR);
        this.propertiesFile = new File(serverDirectory, Constants.SERVER_PROPERTIES_FILE);
        
        validateServerSetup();
    }
    
    /**
     * Validates that the server directory and properties file exist.
     * 
     * @throws IllegalStateException if validation fails
     */
    private void validateServerSetup() {
        if (!serverDirectory.exists() || !serverDirectory.isDirectory()) {
            throw new IllegalStateException("Server directory does not exist: " + Constants.SERVER_DIR);
        }
        
        if (!propertiesFile.exists() || !propertiesFile.isFile()) {
            throw new IllegalStateException("Server properties file does not exist: " + propertiesFile.getAbsolutePath());
        }
        
        LoggerUtil.debug("Server setup validated successfully");
    }
    
    /**
     * Reads the current level-name from server.properties.
     * This is used to identify which world directories need to be cleaned up.
     * 
     * @return The current level-name, or "world" as fallback
     */
    public String getCurrentLevelName() {
        try {
            List<String> lines = Files.readAllLines(propertiesFile.toPath());
            
            for (String line : lines) {
                if (line.startsWith("level-name=")) {
                    String levelName = line.split("=", 2)[1];
                    LoggerUtil.debug("Found current level-name: " + levelName);
                    return levelName;
                }
            }
            
            LoggerUtil.warning("No level-name found in server.properties, using default 'world'");
            return "world";
            
        } catch (IOException e) {
            LoggerUtil.severe("Failed to read current level-name from server.properties", e);
            return "world";
        }
    }
    
    /**
     * Updates the server.properties file with a new world seed and level name.
     * This ensures that each world reset creates completely fresh worlds.
     * 
     * @return true if the update was successful, false otherwise
     */
    public boolean updateWorldConfiguration() {
        try {
            // Generate new world configuration
            WorldConfiguration newConfig = generateNewWorldConfiguration();
            
            // Read current properties
            List<String> lines = Files.readAllLines(propertiesFile.toPath());
            
            // Update or add the configuration lines
            boolean seedUpdated = false;
            boolean nameUpdated = false;
            
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                
                if (line.startsWith("level-seed=")) {
                    lines.set(i, "level-seed=" + newConfig.getSeed());
                    LoggerUtil.info("Updated world seed to: " + newConfig.getSeed());
                    seedUpdated = true;
                } else if (line.startsWith("level-name=")) {
                    lines.set(i, "level-name=" + newConfig.getLevelName());
                    LoggerUtil.info("Updated level-name to: " + newConfig.getLevelName());
                    nameUpdated = true;
                }
            }
            
            // Add missing configuration lines
            if (!seedUpdated) {
                lines.add("level-seed=" + newConfig.getSeed());
                LoggerUtil.info("Added new world seed: " + newConfig.getSeed());
            }
            
            if (!nameUpdated) {
                lines.add("level-name=" + newConfig.getLevelName());
                LoggerUtil.info("Added new level-name: " + newConfig.getLevelName());
            }
            
            // Write the updated properties back to file
            Files.write(propertiesFile.toPath(), lines);
            LoggerUtil.logSuccess("Successfully updated server.properties with new world configuration");
            
            return true;
            
        } catch (IOException e) {
            LoggerUtil.severe("Failed to update world configuration in server.properties", e);
            return false;
        }
    }
    
    /**
     * Generates a new world configuration with unique seed and level name.
     * Uses multiple entropy sources to ensure truly random seeds.
     * 
     * @return A new WorldConfiguration object
     */
    private WorldConfiguration generateNewWorldConfiguration() {
        // Generate truly random seed using multiple entropy sources
        long systemTime = System.nanoTime();
        long currentTime = System.currentTimeMillis();
        double random1 = Math.random() * 1000000;
        double random2 = Math.random() * 1000000;
        long memoryState = Runtime.getRuntime().freeMemory();
        
        // Combine all entropy sources
        long combinedEntropy = systemTime + currentTime + (long)random1 + (long)random2 + memoryState;
        Random random = new Random(combinedEntropy);
        long newSeed = random.nextLong();
        
        // Create unique level name using timestamp modulo
        String uniqueLevelName = Constants.WORLD_NAME_PREFIX + (currentTime % 100000);
        
        LoggerUtil.debug("Generated new world configuration - Seed: " + newSeed + ", Level: " + uniqueLevelName);
        
        return new WorldConfiguration(newSeed, uniqueLevelName);
    }
    
    /**
     * Gets the server directory File object.
     * 
     * @return The server directory
     */
    public File getServerDirectory() {
        return serverDirectory;
    }
    
    /**
     * Gets the server properties File object.
     * 
     * @return The server properties file
     */
    public File getPropertiesFile() {
        return propertiesFile;
    }
    
    /**
     * Inner class to hold world configuration data.
     */
    private static class WorldConfiguration {
        private final long seed;
        private final String levelName;
        
        public WorldConfiguration(long seed, String levelName) {
            this.seed = seed;
            this.levelName = levelName;
        }
        
        public long getSeed() {
            return seed;
        }
        
        public String getLevelName() {
            return levelName;
        }
    }
}
