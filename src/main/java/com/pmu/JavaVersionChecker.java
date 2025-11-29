package com.pmu;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class to check and manage Java versions on the system.
 * Scans common installation directories for Java 21 and validates versions.
 */
public class JavaVersionChecker {

    private static final int REQUIRED_MAJOR_VERSION = 21;
    private static final String JAVA_HOME_ENV = "JAVA_HOME";

    /**
     * Checks if the current Java version is compatible (Java 21 or later)
     */
    public static boolean isCurrentVersionCompatible() {
        String version = System.getProperty("java.version");
        return extractMajorVersion(version) >= REQUIRED_MAJOR_VERSION;
    }

    /**
     * Gets the current Java version string
     */
    public static String getCurrentJavaVersion() {
        return System.getProperty("java.version");
    }

    /**
     * Extracts major version from version string (e.g., "21.0.3" -> 21)
     */
    private static int extractMajorVersion(String versionString) {
        if (versionString == null || versionString.isEmpty()) {
            return -1;
        }
        try {
            // Handle both old format (1.8.0) and new format (21.0.3)
            if (versionString.startsWith("1.")) {
                return Integer.parseInt(versionString.split("\\.")[1]);
            } else {
                return Integer.parseInt(versionString.split("\\.")[0]);
            }
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Scans the system for Java 21 installations
     * @return List of paths to potential Java 21 installations
     */
    public static List<Path> scanForJava21() {
        List<Path> foundPaths = new ArrayList<>();
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

        if (isWindows) {
            foundPaths.addAll(scanWindowsJavaLocations());
        } else {
            foundPaths.addAll(scanUnixJavaLocations());
        }

        // Filter to only valid Java installations
        return foundPaths.stream()
                .filter(JavaVersionChecker::isValidJavaInstallation)
                .collect(Collectors.toList());
    }

    /**
     * Scans common Windows Java installation directories
     */
    private static List<Path> scanWindowsJavaLocations() {
        List<Path> candidates = new ArrayList<>();
        String programFiles = System.getenv("PROGRAMFILES");
        String programFilesX86 = System.getenv("PROGRAMFILES(X86)");

        // Common installation paths
        if (programFiles != null) {
            candidates.add(Paths.get(programFiles, "Java"));
            candidates.add(Paths.get(programFiles, "jdk-21"));
            candidates.add(Paths.get(programFiles, "jdk-21.0.0"));
            candidates.add(Paths.get(programFiles, "openjdk"));
        }

        if (programFilesX86 != null) {
            candidates.add(Paths.get(programFilesX86, "Java"));
            candidates.add(Paths.get(programFilesX86, "jdk-21"));
        }

        // Also check direct paths like C:\Program Files\Java
        candidates.add(Paths.get("C:\\Program Files\\Java"));
        candidates.add(Paths.get("C:\\jdk-21"));

        List<Path> validPaths = new ArrayList<>();
        for (Path candidate : candidates) {
            if (Files.exists(candidate) && Files.isDirectory(candidate)) {
                if (isVersionMatch(candidate, REQUIRED_MAJOR_VERSION)) {
                    validPaths.add(candidate);
                } else {
                    // Scan subdirectories for JDK installations
                    validPaths.addAll(scanDirectoryForJDK(candidate, REQUIRED_MAJOR_VERSION));
                }
            }
        }
        return validPaths;
    }

    /**
     * Scans common Unix/Linux Java installation directories
     */
    private static List<Path> scanUnixJavaLocations() {
        List<Path> candidates = new ArrayList<>();
        String[] unixPaths = {
                "/usr/lib/jvm",
                "/usr/local/java",
                "/opt/java",
                "/opt/jdk",
                System.getProperty("user.home") + "/.jdks",
                System.getProperty("user.home") + "/java"
        };

        List<Path> validPaths = new ArrayList<>();
        for (String pathStr : unixPaths) {
            Path candidate = Paths.get(pathStr);
            if (Files.exists(candidate) && Files.isDirectory(candidate)) {
                if (isVersionMatch(candidate, REQUIRED_MAJOR_VERSION)) {
                    validPaths.add(candidate);
                } else {
                    validPaths.addAll(scanDirectoryForJDK(candidate, REQUIRED_MAJOR_VERSION));
                }
            }
        }
        return validPaths;
    }

    /**
     * Recursively scans a directory for JDK installations matching the required version
     */
    private static List<Path> scanDirectoryForJDK(Path dir, int requiredVersion) {
        List<Path> found = new ArrayList<>();
        try {
            Files.list(dir)
                    .filter(Files::isDirectory)
                    .limit(20) // Limit to prevent excessive scanning
                    .forEach(subDir -> {
                        if (isVersionMatch(subDir, requiredVersion)) {
                            found.add(subDir);
                        }
                    });
        } catch (Exception e) {
            // Silently skip directories we can't access
        }
        return found;
    }

    /**
     * Checks if a directory is a valid Java installation with matching version
     */
    private static boolean isVersionMatch(Path javaDir, int requiredVersion) {
        String dirName = javaDir.getFileName().toString().toLowerCase();
        // Check if directory name contains version info
        if (dirName.contains("jdk") || dirName.contains("java")) {
            if (dirName.contains(String.valueOf(requiredVersion))) {
                return Files.exists(javaDir.resolve("bin"));
            }
        }
        return false;
    }

    /**
     * Checks if a path is a valid Java installation
     */
    private static boolean isValidJavaInstallation(Path javaDir) {
        // Check if bin/java exists
        Path javaBinary = javaDir.resolve("bin").resolve(getJavaBinaryName());
        return Files.exists(javaBinary) && Files.isExecutable(javaBinary);
    }

    /**
     * Gets the platform-specific Java binary name
     */
    private static String getJavaBinaryName() {
        return System.getProperty("os.name").toLowerCase().contains("win") ? "java.exe" : "java";
    }

    /**
     * Attempts to set JAVA_HOME to the specified path
     */
    public static void setJavaHome(Path javaPath) {
        try {
            // For persistent environment variable (Windows)
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                ProcessBuilder pb = new ProcessBuilder(
                        "reg", "add",
                        "HKEY_CURRENT_USER\\Environment",
                        "/v", "JAVA_HOME",
                        "/t", "REG_SZ",
                        "/d", javaPath.toString(),
                        "/f"
                );
                Process process = pb.start();
                process.waitFor();
                System.out.println("JAVA_HOME set to: " + javaPath);
            }
            // For current session
            System.setProperty("JAVA_HOME", javaPath.toString());
        } catch (Exception e) {
            System.out.println("Warning: Could not set JAVA_HOME: " + e.getMessage());
        }
    }

    /**
     * Finds and returns the best Java 21 installation
     */
    public static Optional<Path> findBestJava21() {
        List<Path> candidates = scanForJava21();
        if (!candidates.isEmpty()) {
            return Optional.of(candidates.get(0));
        }
        return Optional.empty();
    }

    /**
     * Prints debug information about Java environment
     */
    public static void printJavaEnvironmentInfo() {
        System.out.println("Current Java Version: " + getCurrentJavaVersion());
        System.out.println("Current JAVA_HOME: " + System.getenv(JAVA_HOME_ENV));
        System.out.println("Java Installation Path: " + System.getProperty("java.home"));
        System.out.println("Is Compatible (Java 21+): " + isCurrentVersionCompatible());
    }
}

