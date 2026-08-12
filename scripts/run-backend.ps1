# SRMS Backend REST API Runner Script

$JDK_PATH = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
$MAVEN_PATH = "C:\tools\apache-maven-3.9.6\bin"

$env:JAVA_HOME = $JDK_PATH
$env:Path = "$env:JAVA_HOME\bin;$MAVEN_PATH;$env:Path"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " Launching SRMS Spring Boot REST API Backend Service..." -ForegroundColor Cyan
Write-Host " Context URL: http://localhost:8080/api/v1" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

mvn spring-boot:run -pl srms-backend -f c:\srms\pom.xml
