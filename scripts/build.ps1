# SRMS Automated Multi-Module Build Script

$JDK_PATH = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
$MAVEN_PATH = "C:\tools\apache-maven-3.9.6\bin"

$env:JAVA_HOME = $JDK_PATH
$env:Path = "$env:JAVA_HOME\bin;$MAVEN_PATH;$env:Path"

Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host " Building SRMS Enterprise Application (Java 21 + Maven)" -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

mvn clean package -DskipTests=false -f c:\srms\pom.xml

if ($LASTEXITCODE -eq 0) {
    Write-Host "BUILD SUCCESSFUL! JAR artifacts created in target directories." -ForegroundColor Green
} else {
    Write-Host "BUILD FAILED! Please check compilation logs above." -ForegroundColor Red
}
