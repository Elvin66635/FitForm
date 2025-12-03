@echo off
chcp 65001 >nul
cd /d "%~dp0"

git add -A
git commit -m "Remove setup instructions file"
git push origin main

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ✓ Pushed successfully!
    timeout /t 2 >nul
    del "%~f0"
) else (
    echo.
    echo ✗ Push failed
    pause
)

