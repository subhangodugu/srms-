# SROTS Desktop Application Multi-Module Launcher Script
$JDK_PATH = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
$MAVEN_PATH = "C:\tools\apache-maven-3.9.6\bin"

$env:JAVA_HOME = $JDK_PATH
$env:Path = "$env:JAVA_HOME\bin;$MAVEN_PATH;$env:Path"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " Launching SROTS Desktop (Strict MVVM + Clean Arch)..." -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

mvn install -DskipTests=true -f c:\srms\srots-desktop\pom.xml
mvn javafx:run -pl srots-app -f c:\srms\srots-desktop\pom.xml
