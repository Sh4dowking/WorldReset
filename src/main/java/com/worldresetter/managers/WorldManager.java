package com.worldresetter.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.worldresetter.config.Constants;
import com.worldresetter.utils.LoggerUtil;

/**
 * Manages world-related operations for the WorldResetter plugin.
 * 
 * This class handles the identification and cleanup of world directories,
 * including the main world, nether, and end dimensions. It also manages
 * cleanup of leftover world directories from previous resets.
 * 
 * @author Sh4dowking
 * @version 1.0.0
 * @since 1.0.0
 */
public class WorldManager {
    
    private final File serverDirectory;
    
    /**
     * Constructs a new WorldManager instance.
     * 
     * @param serverDirectory The server directory containing world files
     * @throws IllegalArgumentException if serverDirectory is null or doesn't exist
     */
    public WorldManager(File serverDirectory) {
        if (serverDirectory == null || !serverDirectory.exists()) {
            throw new IllegalArgumentException("Server directory must exist: " + serverDirectory);
        }
        this.serverDirectory = serverDirectory;
        LoggerUtil.debug("WorldManager initialized for directory: " + serverDirectory.getAbsolutePath());
    }
    
    /**
     * Gets a list of world directories that should be deleted for the given level name.
     * This includes the main world, nether, and end dimensions.
     * 
     * @param levelName The current level name from server.properties
     * @return A list of world directory names to delete
     */
    public List<String> getWorldDirectoriesToDelete(String levelName) {
        List<String> worldDirectories = new ArrayList<>();
        
        // Add the main world directories
        worldDirectories.add(levelName);
        worldDirectories.add(levelName + Constants.NETHER_SUFFIX);
        worldDirectories.add(levelName + Constants.END_SUFFIX);
        
        LoggerUtil.debug("World directories to delete: " + worldDirectories);
        return worldDirectories;
    }
    
    /**
     * Gets a list of old world directories that should be cleaned up.
     * This finds any leftover world directories from previous resets.
     * 
     * @param currentLevelName The current level name to exclude from cleanup
     * @return A list of old world directory names to clean up
     */
    public List<String> getOldWorldDirectories(String currentLevelName) {
        List<String> oldDirectories = new ArrayList<>();
        
        File[] files = serverDirectory.listFiles();
        if (files == null) {
            LoggerUtil.warning("Could not list server directory contents");
            return oldDirectories;
        }
        
        for (File file : files) {
            if (file.isDirectory()) {
                String name = file.getName();
                
                // Check for old world directories
                if (name.startsWith(Constants.WORLD_NAME_PREFIX) && !name.equals(currentLevelName)) {
                    // Skip current world directories
                    if (!name.equals(currentLevelName + Constants.NETHER_SUFFIX) && 
                        !name.equals(currentLevelName + Constants.END_SUFFIX)) {
                        oldDirectories.add(name);
                        LoggerUtil.debug("Found old world directory: " + name);
                    }
                }
            }
        }
        
        LoggerUtil.info("Found " + oldDirectories.size() + " old world directories to clean up");
        return oldDirectories;
    }
    
    /**
     * Validates that world directories exist before attempting deletion.
     * This is used for logging and verification purposes.
     * 
     * @param worldDirectories List of world directory names to validate
     * @return List of directory names that actually exist
     */
    public List<String> validateWorldDirectories(List<String> worldDirectories) {
        List<String> existingDirectories = new ArrayList<>();
        
        for (String dirName : worldDirectories) {
            File worldDir = new File(serverDirectory, dirName);
            if (worldDir.exists() && worldDir.isDirectory()) {
                existingDirectories.add(dirName);
                LoggerUtil.debug("Validated world directory: " + dirName);
            } else {
                LoggerUtil.debug("World directory does not exist: " + dirName);
            }
        }
        
        return existingDirectories;
    }
    
    /**
     * Gets the list of cache files that should be deleted during world reset.
     * These files contain world-specific data that should be cleared.
     * 
     * @return Array of cache file names
     */
    public String[] getCacheFiles() {
        return Constants.CACHE_FILES.clone();
    }
    
    /**
     * Gets the list of cache directories that should be deleted during world reset.
     * These directories contain temporary data that should be cleared.
     * 
     * @return Array of cache directory names
     */
    public String[] getCacheDirectories() {
        return Constants.CACHE_DIRECTORIES.clone();
    }
    
    /**
     * Gets the list of world data file patterns for cleanup.
     * These patterns match files that contain world-specific data.
     * 
     * @return Array of file patterns
     */
    public String[] getWorldDataPatterns() {
        return Constants.WORLD_DATA_PATTERNS.clone();
    }
    
    /**
     * Checks if a directory appears to be a world directory based on its name.
     * 
     * @param directoryName The name of the directory to check
     * @return true if the directory appears to be a world directory
     */
    public boolean isWorldDirectory(String directoryName) {
        return directoryName.startsWith(Constants.WORLD_NAME_PREFIX) ||
               directoryName.equals("world") ||
               directoryName.equals("world_nether") ||
               directoryName.equals("world_the_end");
    }
    
    /**
     * Gets statistics about world directories in the server folder.
     * This is useful for logging and debugging purposes.
     * 
     * @return A WorldStats object containing directory statistics
     */
    public WorldStats getWorldStats() {
        File[] files = serverDirectory.listFiles();
        if (files == null) {
            return new WorldStats(0, 0, 0);
        }
        
        int totalWorldDirs = 0;
        int currentWorldDirs = 0;
        int oldWorldDirs = 0;
        
        for (File file : files) {
            if (file.isDirectory() && isWorldDirectory(file.getName())) {
                totalWorldDirs++;
                
                String name = file.getName();
                if (name.equals("world") || name.equals("world_nether") || name.equals("world_the_end")) {
                    currentWorldDirs++;
                } else {
                    oldWorldDirs++;
                }
            }
        }
        
        return new WorldStats(totalWorldDirs, currentWorldDirs, oldWorldDirs);
    }
    
    /**
     * Inner class to hold world statistics.
     */
    public static class WorldStats {
        private final int totalWorldDirectories;
        private final int currentWorldDirectories;
        private final int oldWorldDirectories;
        
        public WorldStats(int total, int current, int old) {
            this.totalWorldDirectories = total;
            this.currentWorldDirectories = current;
            this.oldWorldDirectories = old;
        }
        
        public int getTotalWorldDirectories() {
            return totalWorldDirectories;
        }
        
        public int getCurrentWorldDirectories() {
            return currentWorldDirectories;
        }
        
        public int getOldWorldDirectories() {
            return oldWorldDirectories;
        }
        
        @Override
        public String toString() {
            return String.format("WorldStats{total=%d, current=%d, old=%d}", 
                               totalWorldDirectories, currentWorldDirectories, oldWorldDirectories);
        }
    }
}
