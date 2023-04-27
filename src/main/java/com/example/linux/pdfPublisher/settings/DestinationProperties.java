package com.example.linux.pdfPublisher.settings;

import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;

import static java.util.Arrays.asList;

import java.util.List;

public class DestinationProperties {

    public DestinationProperties() {}

    // Properties to set up host-domain client server
    public static final String HELLO_KEY = "sonar.example.hello"; // key
    public static final String CONFLUENCE_DESTINATION_CATEGORY = "Confluence Destination Properties";

    // Keys from UI
    public static final String CONFLUENCE_LINUXPOLSKA_HOSTNAME = "confluence.linuxpolska.hostname";
    public static final String CONFLUENCE_LINUXPOLSKA_PORT = "confluence.linuxpolska.port";
    public static final String CONFLUENCE_LINUXPOLSKA_PAGEID = "confluence.linuxpolska.pageid";
    public static final String CONFLUENCE_LINUXPOLSKA_LOGIN = "confluence.linuxpolska.login";
    public static final String CONFLUENCE_LINUXPOLSKA_PASSWORD = "confluence.linuxpolska.password";
    public String hostnameDestination;
    public String loginDestination;
    public String passwordDestination;
    public String portDestination;
    public String pageId;
    // Header
    public static final String X_ATLASSINA_TOKEN = "X-Atlassian-Token";
    public static final String NO_CHECK = "nocheck";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String TEXT_PLAIN = "text/plain";

    //HTTP THE REQUEST METHOD
    public static final String DESTINATION_POST = "POST";
    public static final String HTTP_1 = "The server is thinking through the request.";
    public static final String HTTP_2 = "Success! The request was successfully completed and the server gave the browser" +
            " the expected response.";
    public static final String HTTP_3 = "Redirected somewhere else.";
    public static final String HTTP_4 = "Client errors: Page not found. The site or page couldn’t be reached.";
    public static final String HTTP_5 = "Server errors: Failure.\"";


    // Authorization
    public static final String AUTHORIZATION = "Authorization";
    public static final String SPACE = " ";
    public static final String BASIC = "Basic";
    public static final String HTTP = "http";
    public static final String DOUBLE_DOTS = ":";
    public static final String SLASH = "/";
    public static final String REST_API_CONTENT = "rest" + SLASH + "api" + SLASH + "content";
    public static final String CHILD_ATTACHMENT = "child" + SLASH + "attachment";
    // Request Body
    public static final String TEXT_BODY = "text/plain";
    public static final String MINOR_EDIT = "minorEdit";
    public static final String COMMENT = "comment";
    public static final String COMMENT_BODY = "Adding report file to the " + DestinationProperties.getProperties().get(0).name()
            + "on Port: " + DestinationProperties.getProperties().get(1).name();

    // BOOLEAN
    public static final String TRUE_TEXT = "true";
    public static final Boolean TRUE_VALUE = true;
    public static final String FALSE_TEXT = "false";
    public static final Boolean FALSE_VALUE = false;

    // Error MGMT
    public static final String FIlE = "file";
    public static final String THE_FIlE = "The file";
    public static final String DOES_NOT_EXIST = " does not exists.";
    public static final String UPLOAD_FILE_ATLASSIAN = "Occurred error during uploading file to the "
            + DestinationProperties.getProperties().get(0).name() + " website of atlassian in pageId: " +
            DestinationProperties.getProperties().get(4).name();

    public static List<PropertyDefinition> getProperties() {

        return asList(
                PropertyDefinition.builder(CONFLUENCE_LINUXPOLSKA_HOSTNAME)
                        .name("hostName")
                        .description("Hostname of source confluence server")
                        .defaultValue(String.valueOf(false))
                        .type(PropertyType.STRING)
                        .category(CONFLUENCE_DESTINATION_CATEGORY)
                        .build(),
                PropertyDefinition.builder(CONFLUENCE_LINUXPOLSKA_PORT)
                        .name("port")
                        .description("Port of source confluence server")
                        .defaultValue(String.valueOf(false))
                        .type(PropertyType.INTEGER)
                        .category(CONFLUENCE_DESTINATION_CATEGORY)
                        .build(),
                PropertyDefinition.builder(CONFLUENCE_LINUXPOLSKA_LOGIN)
                        .name("login")
                        .description("Login of confluence server account")
                        .defaultValue(String.valueOf(false))
                        .type(PropertyType.USER_LOGIN)
                        .category(CONFLUENCE_DESTINATION_CATEGORY)
                        .build(),
                PropertyDefinition.builder(CONFLUENCE_LINUXPOLSKA_PASSWORD)
                        .name("password")
                        .description("Password of confluence server account")
                        .defaultValue(String.valueOf(false))
                        .type(PropertyType.PASSWORD)
                        .category(CONFLUENCE_DESTINATION_CATEGORY)
                        .build(),
                PropertyDefinition.builder(CONFLUENCE_LINUXPOLSKA_PAGEID)
                        .name("pageId")
                        .description("Page id of confluence server account")
                        .defaultValue(String.valueOf(false))
                        .type(PropertyType.INTEGER)
                        .category(CONFLUENCE_DESTINATION_CATEGORY)
                        .build());
    }

    public static String urlDestinationBuilder(String hostnameDestination, String portDestination, String pageId) {
        return new StringBuffer()
                .append(DestinationProperties.HTTP)
                .append(DestinationProperties.DOUBLE_DOTS)
                .append(DestinationProperties.SLASH).append(DestinationProperties.SLASH)
                .append(hostnameDestination)
                .append(DestinationProperties.DOUBLE_DOTS)
                .append(portDestination)
                .append(DestinationProperties.SLASH)
                .append(DestinationProperties.REST_API_CONTENT)
                .append(DestinationProperties.SLASH)
                .append(pageId)
                .append(DestinationProperties.SLASH)
                .append(DestinationProperties.CHILD_ATTACHMENT)
                .toString();
    }

    public String getHostnameDestination() {
        return hostnameDestination;
    }

    public void setHostnameDestination(String hostnameDestination) {
        this.hostnameDestination = hostnameDestination;
    }

    public String getLoginDestination() {
        return loginDestination;
    }

    public void setLoginDestination(String loginDestination) {
        this.loginDestination = loginDestination;
    }

    public String getPasswordDestination() {
        return passwordDestination;
    }

    public void setPasswordDestination(String passwordDestination) {
        this.passwordDestination = passwordDestination;
    }

    public String getPortDestination() {
        return portDestination;
    }

    public void setPortDestination(String portDestination) {
        this.portDestination = portDestination;
    }

    public String getPageId() {
        return pageId;
    }

    public void setPageId(String pageId) {
        this.pageId = pageId;
    }
}
