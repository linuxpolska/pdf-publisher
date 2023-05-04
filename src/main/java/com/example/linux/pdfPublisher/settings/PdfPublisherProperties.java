package com.example.linux.pdfPublisher.settings;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.List;

import static java.util.Arrays.asList;

public class PdfPublisherProperties {
    /*
    Empty constructor
    */
    public PdfPublisherProperties() {
        // Only for initialization
    }

    private static final Logger LOGGER = Loggers.get(PdfPublisherProperties.class);
    public static final String DOT_EXTENSION = ".pdf";
    public static final String CATEGORY_PDF_PUBLISHER_PROPERTIES = "Pdf Publisher Properties";
    // Keys from UI SonarQube
    public static final String PDF_PUBLISHER_SONARQUBE_HOSTNAME = "publisher.sonar.hostname";
    public static final String PDF_PUBLISHER_SONARQUBE_LOGIN = "publisher.sonar.login";
    public static final String PDF_PUBLISHER_SONARQUBE_PASSWORD = "publisher.sonar.password";

    // Keys from UI Confluence
    public static final String PDF_PUBLISHER_CONFLUENCE_HOSTNAME = "publisher.confluence.hostname";
    public static final String PDF_PUBLISHER_CONFLUENCE_PAGEID = "publisher.confluence.page_id";
    public static final String PDF_PUBLISHER_CONFLUENCE_LOGIN = "publisher.confluence.login";
    public static final String PDF_PUBLISHER_CONFLUENCE_PASSWORD = "publisher.confluence.password";

    // Properties to set up host-domain server-client SonarQube
    private String projectSonarQubeName;
    private String branchSonarQubeName;
    public String hostnameSonarQubeSource;
    public String loginSonarQube;
    public String passwordSonarQube;

    public String hostnameConfluence;
    public String loginConfluence;
    public String passwordConfluence;
    public String pageIdConfluence;
    public static final String DOUBLE_DOTS = ":";
    public static final String SLASH = "/";
    public static final String API = "api";
    public static final String SECURITY_REPORTS = "security_reports";
    public static final String DOWNLOAD = "download";
    public static final String COMPONENT = "component";
    public static final String QUESTION_MARK = "?";
    public static final String PROJECT = "project";
    public static final String BRANCH = "branch";
    public static final String EQUAL = "=";
    public static final String AND = "&";
    public static final String KEYS = "keys";
    // HTTP settings
    public static final String GET = "GET";
    public static final String AUTHORIZATION = "Authorization";
    public static final String BASIC = "Basic";
    public static final String SPACE = " ";
    // Error Mngt
    public static final String DOES_NOT_EXIST = " does not exists.";
    public static final String UPLOAD_FILE_SONARQUBE = "Occurred error during downloading file to the plugin"
            + " of SonarQube project";

    //HTTP THE REQUEST METHOD
    public static final String CONFULENCE_POST = "POST";
    public static final String HTTP_1 = "The server is thinking through the request.";
    public static final String HTTP_2 = "Success! The request was successfully completed and the server gave the browser" +
            " the expected response.";
    public static final String HTTP_3 = "Redirected somewhere else.";
    public static final String HTTP_4 = "Client errors: Page not found. The site or page couldn’t be reached.";
    public static final String HTTP_5 = "Server errors: Failure.\"";
    // Confluence static values
    // Header
    public static final String X_ATLASSINA_TOKEN = "X-Atlassian-Token";
    public static final String NO_CHECK = "nocheck";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String TEXT_PLAIN = "text/plain";
    // Authorization
    public static final String REST_API_CONTENT = "rest" + SLASH + "api" + SLASH + "content";
    public static final String REST_SETTINGS_VALUES = "/api/settings/values";
    public static final String CHILD_ATTACHMENT = "child" + SLASH + "attachment";
    // Request Body
    public static final String TEXT_BODY = "text/plain";
    public static final String MINOR_EDIT = "minorEdit";
    public static final String COMMENT = "comment";
    public static final String COMMENT_BODY = "Adding report file to the " + PdfPublisherProperties.getProperties().get(3).name();
    // BOOLEAN
    public static final String TRUE_TEXT = "true";
    public static final Boolean TRUE_VALUE = true;
    public static final String FALSE_TEXT = "false";
    public static final Boolean FALSE_VALUE = false;
    // Error MGMT
    public static final String FIlE = "file";
    public static final String THE = "The ";
    public static final String UPLOAD_FILE_ATLASSIAN = "Occurred error during uploading file to the "
            + PdfPublisherProperties.getProperties().get(3).name() + " website of atlassian in pageId: " +
            PdfPublisherProperties.getProperties().get(6).name();

    public String urlSourceBuilder(String hostname_source, String projectName, String branch) {
        if (hostname_source == null) {
            LOGGER.warn("Source Hostname is invalid! Please check the configuration.");
        } else if (projectName == null) {
            LOGGER.warn("Value of projectName name is invalid! Please check the configuration.");
        }
        LOGGER.info("Branch name is : " + branch);
        StringBuilder stringBuilder = new StringBuilder()
                .append(hostname_source);
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
        setHostnameSonarQube(hostname_source);
        LOGGER.info("SonarQube url is: ");
        LOGGER.info(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static List<PropertyDefinition> getProperties() {

        return asList(
                PropertyDefinition.builder(PDF_PUBLISHER_SONARQUBE_HOSTNAME) // 0
                        .name("Sonarqube hostname")
                        .description("Hostname of source sonarqube server example 'https://www.hostname:port'")
                        .defaultValue("SonarQube hostname")
                        .type(PropertyType.STRING)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build(),
                PropertyDefinition.builder(PDF_PUBLISHER_SONARQUBE_LOGIN)  // 1
                        .name("SonarQube login")
                        .description("Login of sonarqube server account")
                        .defaultValue("SonarQube login")
//                        .type(PropertyType.USER_LOGIN)
                        .type(PropertyType.STRING)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build(),
                PropertyDefinition.builder(PDF_PUBLISHER_SONARQUBE_PASSWORD)  // 2
                        .name("SonarQube password")
                        .description("Password of sonarqube server account")
                        .defaultValue("SonarQube password")
                        .type(PropertyType.PASSWORD)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build(),
                PropertyDefinition.builder(PDF_PUBLISHER_CONFLUENCE_HOSTNAME) // 3
                        .name("Confluence hostname")
                        .description("Hostname of source confluence server with 'https://www.hostname:port")
                        .defaultValue("Confluence hostname")
                        .type(PropertyType.STRING)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build(),
                PropertyDefinition.builder(PDF_PUBLISHER_CONFLUENCE_LOGIN) // 4
                        .name("Confluence login")
                        .description("Login of confluence server account")
                        .defaultValue("Confluence login")
//                        .type(PropertyType.USER_LOGIN)
                        .type(PropertyType.STRING)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build(),
                PropertyDefinition.builder(PDF_PUBLISHER_CONFLUENCE_PASSWORD) // 5
                        .name("Confluence password")
                        .description("Password of confluence server account")
                        .defaultValue("Confluence password")
                        .type(PropertyType.PASSWORD)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build(),
                PropertyDefinition.builder(PDF_PUBLISHER_CONFLUENCE_PAGEID) // 6
                        .name("Confluence pageId")
                        .description("Page id of confluence server account. Go to Confluence Page->" +
                                "Click on Three Dots-> Page Information->" +
                                "in the url you will find the PageId parameter values.")
                        .onlyOnQualifiers(Qualifiers.PROJECT)
                        .type(PropertyType.INTEGER)
                        .category(CATEGORY_PDF_PUBLISHER_PROPERTIES)
                        .build());
    }

    public void setHostnameSonarQube(String hostnameSource) {
        this.hostnameSonarQubeSource = hostnameSource;
    }

    public String getHostnameSonarQube() {
        return this.hostnameSonarQubeSource;
    }

    public void setProjectSonarQubeName(String projectSonarQubeName) {
        this.projectSonarQubeName = projectSonarQubeName;
    }

    public String getProjectSonarQubeName() {
        return this.projectSonarQubeName;
    }

    public String getBranchSonarQubeName() {
        return branchSonarQubeName;
    }

    public void setBranchSonarQubeName(String branchSonarQubeName) {
        this.branchSonarQubeName = branchSonarQubeName;
    }

    public String getHostnameSonarQubeSource() {
        return hostnameSonarQubeSource;
    }

    public void setHostnameSonarQubeSource(String hostnameSonarQubeSource) {
        this.hostnameSonarQubeSource = hostnameSonarQubeSource;
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

    public String getHostnameConfluence() {
        return hostnameConfluence;
    }

    public void setHostnameConfluence(String hostnameConfluence) {
        this.hostnameConfluence = hostnameConfluence;
    }

    public String getLoginConfluence() {
        return loginConfluence;
    }

    public void setLoginConfluence(String loginConfluence) {
        this.loginConfluence = loginConfluence;
    }

    public String getPasswordConfluence() {
        return passwordConfluence;
    }

    public void setPasswordConfluence(String passwordConfluence) {
        this.passwordConfluence = passwordConfluence;
    }

    public String getPageIdConfluence() {
        return pageIdConfluence;
    }

    public void setPageIdConfluence(String pageIdConfluence) {
        this.pageIdConfluence = pageIdConfluence;
    }

    public static String urlConfluenceBuilder(String hostnameDestination, String pageId) {
        String url = new StringBuffer()
                .append(hostnameDestination)
                .append(PdfPublisherProperties.SLASH)
                .append(PdfPublisherProperties.REST_API_CONTENT)
                .append(PdfPublisherProperties.SLASH)
                .append(pageId)
                .append(PdfPublisherProperties.SLASH)
                .append(PdfPublisherProperties.CHILD_ATTACHMENT)
                .toString();
        return url;
    }

    public String urlSourcePageIDBuilder(String hostnameSonarQubeSource, String projectSonarQubeName) {
        if (hostnameSonarQubeSource == null) {
            LOGGER.warn("Source Hostname is invalid! Please check the configuration.");
        } else if (projectSonarQubeName == null) {
            LOGGER.warn("Value of projectName name is invalid! Please check the configuration.");
        }
        String url = new StringBuffer()
                .append(hostnameSonarQubeSource)
                .append(PdfPublisherProperties.SLASH)
                .append(PdfPublisherProperties.REST_SETTINGS_VALUES)
                .append(PdfPublisherProperties.QUESTION_MARK)
                .append(PdfPublisherProperties.COMPONENT)
                .append(PdfPublisherProperties.EQUAL)
                .append(projectSonarQubeName)
                .append(PdfPublisherProperties.AND)
                .append(PdfPublisherProperties.KEYS)
                .append(PdfPublisherProperties.EQUAL)
                .append(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_PAGEID)
                .toString();
        return url;
    }
}
