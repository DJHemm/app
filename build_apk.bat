@echo off
REM Loyalty Cards APK Builder for Windows
REM ======================================

SETLOCAL ENABLEDELAYEDEXPANSION

set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

echo ======================================
echo Loyalty Cards APK Builder
echo ======================================
echo.

REM Check if gradlew exists
if not exist "gradlew.bat" (
    echo gradlew.bat not found!
    echo Please run this script from the project root directory
    pause
    exit /b 1
)

echo Building Debug APK...
echo.

REM Build debug APK
call gradlew.bat assembleDebug

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ❌ Build failed!
    pause
    exit /b 1
)

echo.
echo ✅ Debug APK built successfully!
echo.
echo APK location: app\build\outputs\apk\debug\app-debug.apk
echo.
echo You can install it on your device using:
echo   adb install app\build\outputs\apk\debug\app-debug.apk
echo.

REM Check if user wants to build release APK
set /p build_release="Do you want to build a release APK? (y/n): "

if /i "%build_release%" EQU "y" (
    echo.
    echo Building Release APK requires a keystore...
    echo.
    
    REM Check if keystore exists
    if exist "app\loyaltycards.keystore" (
        echo Keystore found in app directory
        echo.
        call gradlew.bat assembleRelease
        
        if %ERRORLEVEL% NEQ 0 (
            echo.
            echo ❌ Release build failed!
            echo Make sure your keystore password is correct in build.gradle
            pause
            exit /b 1
        )
        
        echo.
        echo ✅ Release APK built successfully!
        echo.
        echo APK location: app\build\outputs\apk\release\app-release.apk
        echo.
    ) else (
        echo No keystore found in app directory
        echo.
        echo To create a keystore, run:
        echo   keytool -genkey -v -keystore app\loyaltycards.keystore ^
        echo     -alias loyaltycards -keyalg RSA -keysize 2048 -validity 10000
        echo.
        echo Then update build.gradle with your keystore details
        echo.
    )
)

pause
