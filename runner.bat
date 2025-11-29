@echo off
setlocal enabledelayedexpansion

REM Ensure console uses UTF-8 so Unicode characters render correctly
chcp 65001 >nul
REM Check whether PowerShell exists to provide colored text; fall back to plain echo when it's not available
where powershell.exe >nul 2>nul
if not errorlevel 1 (set "USE_POWERSHELL=1") else (set "USE_POWERSHELL=0")

REM ============================================================================
REM Unified Software Testing Project Runner
REM This script handles Java 21 detection, JAR building, and application launch
REM Works from any directory with no hardcoded paths
REM ============================================================================

REM Get the directory where this script is located
cd /d "%~dp0"

echo.
echo ========================================
echo Software Testing Project Runner
echo ========================================
echo.

REM ============================================================================
REM STEP 1: Check and Auto-Detect Java 21
REM ============================================================================
echo [1/4] Checking Java version...
echo.

REM Try to find Java 21 in common locations if JAVA_HOME is not set or points to wrong version
set "ORIGINAL_JAVA_HOME=%JAVA_HOME%"
set "FOUND_JAVA_21="

REM Check if current Java is already 21+
java -version 2>&1 | findstr "version" | findstr /R /C:"2[1-9]\." >nul
if not errorlevel 1 (
    set "FOUND_JAVA_21=1"
    if "%USE_POWERSHELL%"=="1" (
        powershell -NoProfile -Command "Write-Host '[OK] Java 21+ detected in PATH' -ForegroundColor Green"
    ) else (
        echo [OK] Java 21+ detected in PATH
    )
    java -version 2>&1 | findstr "version"
    echo.
) else (
    echo Current Java version:
    java -version 2>&1 | findstr "version"
    echo.
    if "%USE_POWERSHELL%"=="1" (
        powershell -NoProfile -Command "Write-Host '[WARN] Warning: Java 21 or later required!' -ForegroundColor Yellow"
    ) else (
        echo [WARN] Warning: Java 21 or later required!
    )
    echo Searching for Java 21 on your system...
    echo.

    REM Search for Java 21 in common locations
    for /d %%D in ("C:\Program Files\Java\jdk-21*") do (
        if exist "%%D\bin\java.exe" (
            set "JAVA_HOME=%%D"
            set "FOUND_JAVA_21=1"
            if "%USE_POWERSHELL%"=="1" (
                powershell -NoProfile -Command "Write-Host '[OK] Found Java 21 at: %%D' -ForegroundColor Green"
            ) else (
                echo [OK] Found Java 21 at: %%D
            )
            goto :java_found
        )
    )

    for /d %%D in ("C:\Program Files\jdk-21*") do (
        if exist "%%D\bin\java.exe" (
            set "JAVA_HOME=%%D"
            set "FOUND_JAVA_21=1"
            if "%USE_POWERSHELL%"=="1" (
                powershell -NoProfile -Command "Write-Host '[OK] Found Java 21 at: %%D' -ForegroundColor Green"
            ) else (
                echo [OK] Found Java 21 at: %%D
            )
            goto :java_found
        )
    )

    for /d %%D in ("%PROGRAMFILES%\Java\jdk-21*") do (
        if exist "%%D\bin\java.exe" (
            set "JAVA_HOME=%%D"
            set "FOUND_JAVA_21=1"
            if "%USE_POWERSHELL%"=="1" (
                powershell -NoProfile -Command "Write-Host '[OK] Found Java 21 at: %%D' -ForegroundColor Green"
            ) else (
                echo [OK] Found Java 21 at: %%D
            )
            goto :java_found
        )
    )

    for /d %%D in ("%PROGRAMFILES%\Eclipse Adoptium\jdk-21*") do (
        if exist "%%D\bin\java.exe" (
            set "JAVA_HOME=%%D"
            set "FOUND_JAVA_21=1"
            if "%USE_POWERSHELL%"=="1" (
                powershell -NoProfile -Command "Write-Host '[OK] Found Java 21 at: %%D' -ForegroundColor Green"
            ) else (
                echo [OK] Found Java 21 at: %%D
            )
            goto :java_found
        )
    )

    :java_found
    if defined FOUND_JAVA_21 (
        set "PATH=!JAVA_HOME!\bin;!PATH!"
        echo.
        echo Updated PATH to use Java 21 for this session.
        echo.
        echo To make this permanent, run in PowerShell as Administrator:
        echo   [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "!JAVA_HOME!", "User"^)
        echo.
    ) else (
        echo.
        if "%USE_POWERSHELL%"=="1" (
            powershell -NoProfile -Command "Write-Host '[ERROR] Java 21 not found on your system!' -ForegroundColor Red"
        ) else (
            echo [ERROR] Java 21 not found on your system!
        )
        echo.
        echo Please download and install Java 21 from:
        echo   - Eclipse Temurin: https://adoptium.net/temurin/releases/
        echo   - Oracle JDK: https://www.oracle.com/java/technologies/downloads/#java21
        echo.
        echo After installation, either:
        echo   1. Set JAVA_HOME environment variable, OR
        echo   2. Ensure Java 21 is in your PATH
        echo.
        pause
        exit /b 1
    )
)

REM ============================================================================
REM STEP 2: Check if JAR exists, offer to build if not
REM ============================================================================
echo [2/4] Checking for application JAR...
echo.

if not exist "target\SoftwareTesting-1.0-SNAPSHOT.jar" (
    if "%USE_POWERSHELL%"=="1" (
        powershell -NoProfile -Command "Write-Host '[ERROR] JAR file not found at: target\SoftwareTesting-1.0-SNAPSHOT.jar' -ForegroundColor Red"
    ) else (
        echo [ERROR] JAR file not found at: target\SoftwareTesting-1.0-SNAPSHOT.jar
    )
    echo.
    echo Would you like to build the project now? (Y/N^)
    set /p BUILD_CHOICE="> "

    if /i "!BUILD_CHOICE!"=="Y" (
        echo.
        echo Building project with Maven...
        call mvn clean package -DskipTests

        if errorlevel 1 (
            echo.
            if "%USE_POWERSHELL%"=="1" (
                powershell -NoProfile -Command "Write-Host '[ERROR] Build failed! Please check the errors above.' -ForegroundColor Red"
            ) else (
                echo [ERROR] Build failed! Please check the errors above.
            )
            pause
            exit /b 1
        )

        echo.
        if "%USE_POWERSHELL%"=="1" (
            powershell -NoProfile -Command "Write-Host '[OK] Build completed successfully!' -ForegroundColor Green"
        ) else (
            echo [OK] Build completed successfully!
        )
        echo.
    ) else (
        echo.
        echo Cannot run without JAR file. Please build with:
        echo   mvn clean package
        echo.
        pause
        exit /b 1
    )
) else (
    if "%USE_POWERSHELL%"=="1" (
        powershell -NoProfile -Command "Write-Host '[OK] JAR file found' -ForegroundColor Green"
    ) else (
        echo [OK] JAR file found
    )
    echo.
)

REM ============================================================================
REM STEP 3: Display Java Environment Info
REM ============================================================================
echo [3/4] Java Environment:
echo.
echo   JAVA_HOME: !JAVA_HOME!
java -version 2>&1 | findstr "version"
echo.

REM ============================================================================
REM STEP 4: Launch Application
REM ============================================================================
echo [4/4] Starting application...
echo.
echo ========================================
echo.

java -jar target\SoftwareTesting-1.0-SNAPSHOT.jar

echo.
echo ========================================
echo Application terminated
echo ========================================
pause
endlocal

