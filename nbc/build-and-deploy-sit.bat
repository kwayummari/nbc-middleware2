@echo off
REM Set the script to exit immediately if any command fails
setlocal enabledelayedexpansion
set "ERRORLEVEL=0"

REM Step 1: Run Maven clean install
echo Running Maven clean install...
call mvn clean install -
if errorlevel 1 (
    echo [ERROR] Maven build failed. Exiting.
    exit /b 1
)

REM Step 2: Build the Docker image
echo Building Docker image...
docker build --no-cache -t  nbc-middleware-sit .
if errorlevel 1 (
    echo [ERROR] Docker build failed. Exiting.
    exit /b 1
)

REM Step 3: Tag the Docker image
echo Tagging Docker image...
docker tag nbc-middleware-sit:latest itrust.registryuat:10000/nbc-middleware-sit:latest
if errorlevel 1 (
    echo [ERROR] Docker tag failed. Exiting.
    exit /b 1
)

REM Step 4: Push the Docker image
echo Pushing Docker image...
docker push itrust.registryuat:10000/nbc-middleware-sit:latest
if errorlevel 1 (
    echo [ERROR] Docker push failed. Exiting.
    exit /b 1
)

echo [SUCCESS] All steps completed successfully.
exit /b 0

