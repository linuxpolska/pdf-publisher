# pdf-publisher
SonarQube Enterprise Plugin - Publish PDF Security Reports to Confluence

## Description / Features

This plugin automatically pushes Security Reports from SonarQube Enterprise to Confluence.

Compatibility matrix:

| Plugin | Sonar min | Sonar max | Jdk |
|--------|-----------|-----------|-----|
| 1.0.3  | 9.0       | 9.0+      | 17  |

## Dependencies

| Dependency Name | Library Name | Library License | Library URL |
|-----------------|--------------|-----------------|--------------|
| OkHttp	| okhttp | Apache 2.0 | [link](https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp) |
| SonarQube | sonar-plugin-api | LGPL 3.0 | [link](https://mvnrepository.com/artifact/org.sonarsource.sonarqube/sonar-plugin-api) |
| Apache HttpClient | httpclient | Apache 2.0 | [link](https://mvnrepository.com/artifact/org.apache.httpcomponents/httpclient) | 
| Apache Commons IO | commons-io maven | Apache 2.0 | [link](https://mvnrepository.com/artifact/commons-io/commons-io) |
| SLF4J API Module | slf4j-api | MIT | [link](https://mvnrepository.com/artifact/org.slf4j/slf4j-api) |
| JUnit | junit | EPL 1.0 | [link](https://mvnrepository.com/artifact/junit/junit) |
| JSON.simple | json-simple | Apache 2.0 | [link](https://mvnrepository.com/artifact/com.googlecode.json-simple/json-simple) |
| Unirest Java | unirest-java | MIT | [link](https://mvnrepository.com/artifact/com.mashape.unirest/unirest-java)) |
| Apache Commons Logging | commons-logging | Apache 2.0 | [link](https://mvnrepository.com/artifact/commons-logging/commons-logging) |
| SonarQube :: Packaging Maven Plugin | sonar-packaging-maven-plugin | LGPL 3.0 | [link](https://mvnrepository.com/artifact/org.sonarsource.sonar-packaging-maven-plugin/sonar-packaging-maven-plugin) |
| Apache Maven Compiler Plugin | maven-compiler-plugin | Apache 2.0 | [link](https://mvnrepository.com/artifact/org.apache.maven.plugins/maven-compiler-plugin) |
| Native2Ascii Maven Plugin | native2ascii-maven-plugin | MIT | [link](https://mvnrepository.com/artifact/org.codehaus.mojo/native2ascii-maven-plugin) |
| Maven Frontend Plugin | frontend-maven-plugin | Apache 2.0 | [link](https://mvnrepository.com/artifact/com.github.eirslett/frontend-maven-plugin) | 



## Usage

Installation:
- download latest or required version from [here](https://github.com/linuxpolska/pdf-publisher/releases)
- place jar file to your SonarQube Enterprise instance to ```[YOUR_SONAR_PATH]/extensions/plugins```
- restart SonarQube Enterprise

In Global Configuration fill in your SonarQube and Confluence URLs and credentials.

In Project Configuration fill in your Confluence PAGE_ID obtained from URL of "Attachments" page.

From now on, on every branch scan PDF Security Reports will be automatically uploaded to Confluence.
