@echo off
REM Set the script to exit immediately if any command fails
setlocal enabledelayedexpansion
set "ERRORLEVEL=0"

REM Step 1: Run Maven clean install
echo Running Maven clean install...
call ..\mvnw clean install
if errorlevel 1 (
    echo [ERROR] Maven build failed. Exiting.
    exit /b 1
)

REM Step 2: Build the Docker image
echo Building Docker image...
docker build -t uat-nbc .
if errorlevel 1 (
    echo [ERROR] Docker build failed. Exiting.
    exit /b 1
)

REM Step 3: Tag the Docker image
echo Tagging Docker image...
docker tag uat-nbc:latest itrust.registry:10000/uat-nbc:latest
if errorlevel 1 (
    echo [ERROR] Docker tag failed. Exiting.
    exit /b 1
)

REM Step 4: Push the Docker image
echo Pushing Docker image...
docker push itrust.registry:10000/uat-nbc:latest
if errorlevel 1 (
    echo [ERROR] Docker push failed. Exiting.
    exit /b 1
)

echo [SUCCESS] All steps completed successfully.
exit /b 0
