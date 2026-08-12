# Run mvn clean verify on srots-desktop
$JDK_PATH = "C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
$MAVEN_PATH = "C:\tools\apache-maven-3.9.6\bin"

$env:JAVA_HOME = $JDK_PATH
$env:Path = "$env:JAVA_HOME\bin;$MAVEN_PATH;$env:Path"

mvn clean verify -f c:\srms\srots-desktop\pom.xml
