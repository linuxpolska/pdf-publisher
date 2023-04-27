package com.example.linux.pdfPublisher.settings;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.List;

import static java.util.Arrays.asList;

public class PdfPublisherProperties {
    public PdfPublisherProperties() {}
    private static final Logger LOGGER = Loggers.get(PdfPublisherProperties.class);
    public static final String DOT_EXTENSION = ".pdf";
    public static final String CATEGORY_PDF_PUBLISHER_PROPERTIES = "Pdf Publisher Properties";
    // Keys from UI
    public static final String SONAR_LINUXPOLSKA_HOSTNAME = "sonar.linuxpolska.hostname";
    public static final String SONAR_LINUXPOLSKA_PORT = "sonar.linuxpolska.port";
    public static final String SONAR_LINUXPOLSKA_LOGIN = "sonar.linuxpolska.login";
    public static final String SONAR_LINUXPOLSKA_PASSWORD = "sonar.linuxpolska.password";

    // Properties to set up host-domain server-client
    private String projectName;
    private String branchName;
    public String hostnameSource;
    public String loginSonarQube;
    public String passwordSonarQube;
    public String portSonarQube;
    public static final String HTTPS = "https";
    public static final String DOUBLE_DOTS = ":";
    public static final String SLASH = "/";
    public static final String API = "api";
    public static final String SECURITY_REPORTS = "security_reports";
    public static final String DOWNLOAD = "download";
    public static final String QUESTION_MARK = "?";
    public static final String PROJECT = "project";
    public static final String BRANCH = "branch";
    public static final String EQUAL = "=";
    public static final String AND = "&";
    // HTTP settings
    public static final String GET = "GET";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BASIC = "Basic";
    public static final String SPACE = " ";

    // Error Mngt
    public static final String DOES_NOT_EXIST = " does not exists.";
    public static final String UPLOAD_FILE_SONARQUBE = "Occurred error during downloading file to the plugin"
            + " of SonarQube project";
    public String urlSourceBuilder(String hostname_source, String sourcePort, String projectName, String branch) {
        if (hostname_source == null) {
            LOGGER.warn("Source Hostname is invalid! Please check the configuration.");
        } else if (projectName == null) {
            LOGGER.warn("Value of projectName name is invalid! Please check the configuration.");
        }
        LOGGER.info("Port number is: " + sourcePort);
        LOGGER.info("Branch name is : " + branch);
        StringBuffer stringBuilder = new StringBuffer()
                .append(PdfPublisherProperties.HTTPS)
                .append(PdfPublisherProperties.DOUBLE_DOTS)
                .append(PdfPublisherProperties.SLASH).append(PdfPublisherProperties.SLASH)
                .append(hostname_source);
        if (sourcePort != null) {
            stringBuilder
                    .append(PdfPublisherProperties.DOUBLE_DOTS)
                    .append(sourcePort);
        }
        stringBuilder
                .append(PdfPublisherProperties.SLASH)
                .append(PdfPublisherProperties.API)
                .append(PdfPublisherProperties.SLASH)
                .append(PdfPublisherProperties.SECURITY_REPORTS)
                .append(PdfPublisherProperties.SLASH)
                .append(PdfPublisherProperties.DOWNLOAD)
                .append(PdfPublisherProperties.QUESTION_MARK);
        if (projectName != null && branch != null) { // report of analysis from projectName of specific branch
            stringBuilder

                    .append(PdfPublisherProperties.PROJECT)
                    .append(PdfPublisherProperties.EQUAL)
                    .append(projectName)
                    .append(PdfPublisherProperties.AND)
                    .append(PdfPublisherProperties.BRANCH)
                    .append(PdfPublisherProperties.EQUAL)
                    .append(branch);
        } else if (projectName != null) { // report of analysis from projectName
            stringBuilder
                    .append(PdfPublisherProperties.PROJECT)
                    .append(PdfPublisherProperties.EQUAL)
                    .append(projectName);
        }
        setHostname(hostname_source);
        return stringBuilder.toString();
    }
    public static List<PropertyDefinition> getProperties() {

        return asList(
                PropertyDefinition.builder(SONAR_LINUXPOLSKA_HOSTNAME)
                        .name("hostName")
                        .description("Hostname of source sonarqube server")
                        .defaultValue(String.valueOf(false))
                        .type(PropertyType.STRING)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build(),
                PropertyDefinition.builder(SONAR_LINUXPOLSKA_PORT)
                        .name("port")
                        .description("Port of source sonarqube server")
                        .defaultValue(String.valueOf(false))
                        .type(PropertyType.INTEGER)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build(),
                PropertyDefinition.builder(SONAR_LINUXPOLSKA_LOGIN)
                        .name("login")
                        .description("Login of sonarqube server account")
                        .defaultValue(String.valueOf(false))
                        .type(PropertyType.USER_LOGIN)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build(),
                PropertyDefinition.builder(SONAR_LINUXPOLSKA_PASSWORD)
                        .name("password")
                        .description("Password of sonarqube server account")
                        .defaultValue(String.valueOf(false))
                        .type(PropertyType.PASSWORD)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build());
    }

    public void setHostname(String hostnameSource) {
        this.hostnameSource = hostnameSource;
    }

    public String getHostname() {
        return this.hostnameSource;
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
    public String getProjectName() {
        return this.projectName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public String getHostnameSource() {
        return hostnameSource;
    }

    public void setHostnameSource(String hostnameSource) {
        this.hostnameSource = hostnameSource;
    }

    public String getLoginSonarQube() {
        return loginSonarQube;
    }

    public void setLoginSonarQube(String loginSonarQube) {
        this.loginSonarQube = loginSonarQube;
    }

    public String getPasswordSonarQube() {
        return passwordSonarQube;
    }

    public void setPasswordSonarQube(String passwordSonarQube) {
        this.passwordSonarQube = passwordSonarQube;
    }

    public String getPortSonarQube() {
        return portSonarQube;
    }

    public void setPortSonarQube(String portSonarQube) {
        this.portSonarQube = portSonarQube;
    }
}
