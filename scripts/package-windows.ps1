# SROTS Windows jpackage Packaging Script

$JDK_PATH = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
$MAVEN_PATH = "C:\tools\apache-maven-3.9.6\bin"

$env:JAVA_HOME = $JDK_PATH
$env:Path = "$env:JAVA_HOME\bin;$MAVEN_PATH;$env:Path"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " Packaging SROTS Windows Desktop Application..." -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

mvn clean package -DskipTests=true -f c:\srms\srots-desktop\pom.xml

$INPUT_DIR = "c:\srms\srots-desktop\srots-app\target"
$OUTPUT_DIR = "c:\srms\deployment\dist"
$MAIN_JAR = "srots-app-0.1.0-SNAPSHOT.jar"

if (-not (Test-Path "$INPUT_DIR\$MAIN_JAR")) {
    Write-Host "SROTS app jar not found at $INPUT_DIR\$MAIN_JAR" -ForegroundColor Red
    exit 1
}

New-Item -ItemType Directory -Force -Path $OUTPUT_DIR | Out-Null

$wixInstalled = Get-Command "candle.exe" -ErrorAction SilentlyContinue

if ($wixInstalled) {
    Write-Host "WiX Toolset detected. Packaging native installer (.exe)..." -ForegroundColor Yellow
    jpackage `
      --name "SROTS" `
      --app-version "0.1.0" `
      --input $INPUT_DIR `
      --main-jar $MAIN_JAR `
      --main-class "com.srots.app.SrotsLauncher" `
      --type exe `
      --dest $OUTPUT_DIR `
      --win-shortcut `
      --win-menu `
      --vendor "SORTS Enterprise Solutions"
} else {
    Write-Host "WiX Toolset not found on PATH. Packaging standalone application image..." -ForegroundColor Yellow
    Write-Host "(To generate a single .exe installer, install WiX v3+ and add candle.exe to PATH)" -ForegroundColor Gray

    jpackage `
      --name "SROTS" `
      --app-version "0.1.0" `
      --input $INPUT_DIR `
      --main-jar $MAIN_JAR `
      --main-class "com.srots.app.SrotsLauncher" `
      --type app-image `
      --dest $OUTPUT_DIR `
      --vendor "SORTS Enterprise Solutions"
}

Write-Host "SROTS Windows Packaging Complete! Output located in $OUTPUT_DIR" -ForegroundColor Green

