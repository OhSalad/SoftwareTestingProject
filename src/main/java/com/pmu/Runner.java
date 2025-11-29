package com.pmu;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

/**
 * A simple runner with a menu that allows the user to run the driver or tests
 * by selecting a number mapped to an action.
 *
 * Features:
 * - Run the GradeCalculatorDriver (in the same JVM)
 * - Run tests programmatically via JUnit Platform Launcher (if test classes are on the classpath)
 * - Run tests by invoking Maven (fallback when JUnit launcher can't discover tests at runtime)
 */
public class Runner {

    private static final String PROJECT_DIR = System.getProperty("user.dir");

    /**
     * Checks the current Java version and attempts to configure Java 21 if not already running on it.
     * This method scans the system for Java 21 installations and provides helpful information.
     */
    private static void checkAndConfigureJavaVersion() {
        System.out.println("========================================");
        System.out.println("Java Version Check");
        System.out.println("========================================");

        JavaVersionChecker.printJavaEnvironmentInfo();
        System.out.println();

        if (JavaVersionChecker.isCurrentVersionCompatible()) {
            System.out.println("✓ Current Java version is compatible (Java 21 or later)");
            System.out.println();
            return; // Already on Java 21 or later
        }

        System.out.println("✗ Current Java version is NOT compatible. This project requires Java 21 or later.");
        System.out.println();
        System.out.println("Scanning your system for Java 21...");

        var java21Candidates = JavaVersionChecker.scanForJava21();

        if (!java21Candidates.isEmpty()) {
            System.out.println("✓ Found " + java21Candidates.size() + " Java 21 installation(s):");
            for (int i = 0; i < java21Candidates.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + java21Candidates.get(i));
            }
            System.out.println();
            System.out.println("NOTE: To use Java 21 in future runs, set your JAVA_HOME environment variable.");
            System.out.println("On Windows, run this command in PowerShell (as Administrator):");
            System.out.println("  [System.Environment]::SetEnvironmentVariable(\"JAVA_HOME\", \"" + java21Candidates.get(0) + "\", \"User\")");
            System.out.println();
            System.out.println("Then restart your command prompt or IDE.");
            System.out.println();
            System.out.println("WARNING: This application may not run correctly with Java " +
                             JavaVersionChecker.getCurrentJavaVersion() + ".");
            System.out.println("Please set JAVA_HOME and restart.");
        } else {
            System.out.println("✗ No Java 21 installation found on your system.");
            System.out.println();
            System.out.println("Download Java 21 from one of these sources:");
            System.out.println("  - Oracle JDK: https://www.oracle.com/java/technologies/downloads/#java21");
            System.out.println("  - Eclipse Temurin (free): https://adoptium.net/temurin/releases/");
            System.out.println();
            System.out.println("After installation, set your JAVA_HOME environment variable.");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // Check Java version and attempt to configure Java 21 if needed
        checkAndConfigureJavaVersion();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();
                System.out.print("Enter selection: ");
                String input;
                try {
                    if (!scanner.hasNextLine()) {
                        System.out.println("No input available; exiting.");
                        break;
                    }
                    input = scanner.nextLine().trim();
                } catch (java.util.NoSuchElementException nse) {
                    System.out.println("No input available; exiting.");
                    break;
                }
                if (input.isEmpty()) continue;

                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid option. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 0:
                        System.out.println("Exiting.");
                        return;
                    case 1:
                        runDriver();
                        break;
                    case 2:
                        runAllTestsWithLauncher();
                        break;
                    case 3:
                        runAllTestsWithMaven();
                        break;
                    case 4:
                        runTestWithLauncher("com.pmu.StudentTest");
                        break;
                    case 5:
                        runTestWithLauncher("com.pmu.CourseTest");
                        break;
                    case 6:
                        runTestWithLauncher("com.pmu.GradeCalculatorTest");
                        break;
                    case 7:
                        runJacocoAndCopyArtifacts();
                        break;
                    default:
                        System.out.println("Unknown selection: " + choice);
                }

                System.out.println();
                System.out.print("Press Enter to continue...");
                try {
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    }
                } catch (java.util.NoSuchElementException nse) {
                    // stdin closed (piped input). Simply continue/exit gracefully
                    System.out.println();
                    return;
                }
            }
        }
    }

    private static void printMenu() {
        System.out.println("----------------------------------------");
        System.out.println("Project Runner - choose an action:");
        System.out.println("0) Exit");
        System.out.println("1) Run GradeCalculator Driver (in-JVM)");
        System.out.println("2) Run All Tests (JUnit Launcher)");
        System.out.println("3) Run All Tests (Maven)");
        System.out.println("4) Run StudentTest (JUnit Launcher / Maven fallback)");
        System.out.println("5) Run CourseTest (JUnit Launcher / Maven fallback)");
        System.out.println("6) Run GradeCalculatorTest (JUnit Launcher / Maven fallback)");
        System.out.println("7) Run JaCoCo (generate coverage and copy artifacts to target/artifacts)");
        System.out.println("----------------------------------------");
    }

    private static void runDriver() {
        System.out.println("Running GradeCalculatorDriver...");
        try {
            GradeCalculatorDriver.main(new String[0]);
        } catch (Exception e) {
            System.out.println("Error running driver: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static void runAllTestsWithLauncher() {
        System.out.println("Attempting to run all tests via JUnit Platform Launcher (requires test classes on classpath)...");
        try {
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(DiscoverySelectors.selectPackage("com.pmu"))
                    .build();

            runLauncherRequest(request);
        } catch (NoClassDefFoundError ncdfe) {
            System.out.println("JUnit Platform libraries not found on classpath: " + ncdfe.getMessage());
            System.out.println("Note: JUnit Launcher requires junit-platform-launcher and related JARs on the classpath.");
            System.out.println("Falling back to Maven 'mvn test'...");
            runAllTestsWithMaven();
        } catch (Exception e) {
            System.out.println("JUnit Launcher failed: " + e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("TestEngine")) {
                System.out.println("Note: Test engines may not be available on the classpath when running from JAR.");
                System.out.println("This is expected behavior - the fallback to Maven will handle test execution.");
            }
            System.out.println("Falling back to Maven 'mvn test'...");
            runAllTestsWithMaven();
        }
    }

    private static void runTestWithLauncher(String testClassName) {
        System.out.println("Attempting to run test via JUnit Platform Launcher: " + testClassName);
        try {
            Class<?> testClass = Class.forName(testClassName);
            LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                    .selectors(DiscoverySelectors.selectClass(testClass))
                    .build();

            runLauncherRequest(request);
        } catch (ClassNotFoundException e) {
            System.out.println("Test class not found on runtime classpath: " + testClassName);
            System.out.println("Falling back to Maven invocation...");
            runMavenTest(testClassName);
        } catch (NoClassDefFoundError ncdfe) {
            System.out.println("JUnit Platform libraries not found on classpath: " + ncdfe.getMessage());
            System.out.println("Falling back to Maven invocation...");
            runMavenTest(testClassName);
        } catch (Exception e) {
            System.out.println("JUnit Launcher failed: " + e.getMessage());
            System.out.println("Falling back to Maven invocation...");
            runMavenTest(testClassName);
        }
    }

    private static void runLauncherRequest(LauncherDiscoveryRequest request) {
        Launcher launcher = LauncherFactory.create();
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);
        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        try (PrintWriter writer = new PrintWriter(System.out, true)) {
            summary.printTo(writer);
            writer.flush();
        }
    }

    private static void runAllTestsWithMaven() {
        System.out.println("Running all tests using Maven (this will spawn a separate process)...");
        runMavenCommand(new String[]{"mvn", "test"});
    }

    private static void runMavenTest(String className) {
        String shortName = className;
        if (className.contains(".")) {
            shortName = className.substring(className.lastIndexOf('.') + 1);
        }
        System.out.printf("Running Maven test for: %s (pattern=%s)%n", className, shortName);
        runMavenCommand(new String[]{"mvn", "-Dtest=" + shortName, "test"});
    }

    private static void runMavenCommand(String[] cmdArray) {
        // Try to execute the requested command; if it fails because the executable isn't found,
        // try a small list of fallbacks commonly used on different platforms.
        String[] fallbackCmds = buildFallbacks(cmdArray[0]);
        boolean started = false;
        for (String cmdExec : fallbackCmds) {
            String[] cmd = cmdArray.clone();
            cmd[0] = cmdExec;
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.directory(new File(PROJECT_DIR));
            pb.redirectErrorStream(true);
            try {
                System.out.println("Running command: " + Arrays.toString(cmd));
                Process process = pb.start();
                started = true;
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                }
                int exit = process.waitFor();
                System.out.println("Maven exit code: " + exit);
                return; // done
            } catch (IOException ioe) {
                // If this is a 'file not found' issue, try the next candidate, otherwise show
                // the error and abort.
                String message = ioe.getMessage();
                if (message == null || message.contains("CreateProcess error=2") || message.contains("error=2") || message.contains("Cannot run program")) {
                    // try the next candidate
                    System.out.println("Command not found: " + cmdExec + "; trying next fallback if available...");
                    continue;
                }
                System.out.println("Failed to run Maven command: " + Arrays.toString(cmd));
                ioe.printStackTrace(System.out);
                return;
            } catch (InterruptedException ie) {
                System.out.println("Maven command was interrupted.");
                ie.printStackTrace(System.out);
                Thread.currentThread().interrupt();
                return;
            }
        }
        if (!started) {
            System.out.println("No Maven executable found in PATH and no wrapper available.");
            System.out.println("Please install Apache Maven or add it to your PATH, or add a Maven wrapper (mvnw/mvnw.cmd) to the project root.");
        }
    }

    private static String[] buildFallbacks(String original) {
        boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");
        java.util.List<String> candidates = new java.util.ArrayList<>();
        // Prefer the original requested command first
        candidates.add(original);
        if (isWindows) {
            // Common Windows alternatives - prioritize .cmd files on Windows
            if (!original.equalsIgnoreCase("mvn.cmd")) candidates.add("mvn.cmd");
            if (!original.equalsIgnoreCase("mvnw.cmd")) candidates.add("mvnw.cmd");
            if (!original.equalsIgnoreCase("mvnw")) candidates.add("mvnw");
            if (!original.equalsIgnoreCase("mvn")) candidates.add("mvn");
        } else {
            // Common *nix alternatives
            if (!original.equalsIgnoreCase("mvnw")) candidates.add("mvnw");
            if (!original.equalsIgnoreCase("./mvnw")) candidates.add("./mvnw");
            if (!original.equalsIgnoreCase("mvn")) candidates.add("mvn");
        }
        return candidates.toArray(new String[0]);
    }

    private static void runJacocoAndCopyArtifacts() {
        System.out.println("Running JaCoCo instrumentation and generating report via Maven...");
        // Run tests (JaCoCo prepare-agent is configured in the POM to attach to test phase)
        runMavenCommand(new String[]{"mvn", "test"});

        // Ensure the JaCoCo site was generated
        File jacocoSite = new File(PROJECT_DIR, "target/site/jacoco");
        File artifactsDir = new File(PROJECT_DIR, "target/artifacts");

        if (!jacocoSite.exists() || !jacocoSite.isDirectory()) {
            System.out.println("JaCoCo site not found at: " + jacocoSite.getAbsolutePath());
            System.out.println("Try running 'mvn test' with JaCoCo configured.");
            return;
        }

        // Create artifacts dir if it doesn't exist
        if (!artifactsDir.exists()) {
            if (!artifactsDir.mkdirs()) {
                System.out.println("Failed to create artifacts directory: " + artifactsDir.getAbsolutePath());
                return;
            }
        }

        // Copy files from target/site/jacoco -> target/artifacts
        try {
            copyDirectory(jacocoSite.toPath(), artifactsDir.toPath());
            System.out.println("Copied JaCoCo artifacts to: " + artifactsDir.getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Failed to copy JaCoCo artifacts: " + e.getMessage());
            e.printStackTrace(System.out);
        }
    }

    private static void copyDirectory(java.nio.file.Path src, java.nio.file.Path dest) throws IOException {
        try (java.util.stream.Stream<java.nio.file.Path> paths = java.nio.file.Files.walk(src)) {
            paths.forEach(sourcePath -> {
                java.nio.file.Path targetPath = dest.resolve(src.relativize(sourcePath));
                try {
                    if (java.nio.file.Files.isDirectory(sourcePath)) {
                        if (!java.nio.file.Files.exists(targetPath)) {
                            java.nio.file.Files.createDirectories(targetPath);
                        }
                    } else {
                        java.nio.file.Files.copy(sourcePath, targetPath, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
