# pdf-publisher
SonarQube Plugin - Publish PDF Security Reports to Confluence

## Description / Features

This plugin automatically pushes Security Reports from SonarQube to Confluence.

Compatibility matrix:

| Checkstyle Plugin | Sonar min | Sonar max | Jdk |
|-------------------|-----------|-----------|-----|
| 1.0.3             | 9.0       | 9.0+      | 17  |

## Dependencies

| Dependency Name | Library Name | Librar License | Library URL |
|-----------------|--------------|----------------|--------------|
| OkHttp	| okhttp | Apache 2.0 | https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp |
| SonarQube | sonar-plugin-api | GNU General Public License (GPL) | https://mvnrepository.com/artifact/org.sonarsource.sonarqube/sonar-plugin-api |
| Apache HttpClient | httpclient | Apache 2.0 | https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient | 
| Apache Commons IO | commons-io maven | Apache 2.0 | https://mvnrepository.com/artifact/commons-io/commons-io |
| SLF4J API Module | slf4j-api | MIT | https://mvnrepository.com/artifact/org.slf4j/slf4j-api |
| JUnit | junit | EPL 1.0 | https://mvnrepository.com/artifact/junit/junit |
| JSON.simple | json-simple | Apache 2.0 | https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple |
| Unirest Java | unirest-java | MIT | https://mvnrepository.com/artifact/com.mashape.unirest/unirest-java |
| Apache Commons Logging | commons-logging | Apache 2.0 | https://mvnrepository.com/artifact/commons-logging/commons-logging |
| SonarQube :: Packaging Maven Plugin | sonar-packaging-maven-plugin | LGPL 3.0 | https://mvnrepository.com/artifact/org.sonarsource.sonar-packaging-maven-plugin/sonar-packaging-maven-plugin |
| Apache Maven Compiler Plugin | maven-compiler-plugin | Apache 2.0 | https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-compiler-plugin |
| Native2Ascii Maven Plugin | native2ascii-maven-plugin | MIT | https://mvnrepository.com/artifact/org.codehaus.mojo/native2ascii-maven-plugin |
| Maven Frontend Plugin | frontend-maven-plugin | Apache 2.0 | https://mvnrepository.com/artifact/com.github.eirslett/frontend-maven-plugin | 



## Usage

Dowload latest or required version from https://github.com/linuxpolska/pdf-publisher/releases .
Place jar file to your sonar instance to "[YOUR_SONAR_PATH]/extensions/plugins", restart Sonar.

In Global Configuration fill in your SonarQube and Confluence URLs and credentials.

In Project Configuration fill in your Confluence PAGE_ID obtained from URL of "Attachments" page.

From now on, on every branch scan PDF Security Reports will be automatically uploaded to Confluence.
