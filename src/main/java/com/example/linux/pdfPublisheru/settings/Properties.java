package com.example.linux.pdfPublisheru.settings;

import org.sonar.api.config.PropertyDefinition;

import static java.util.Arrays.asList;

import java.util.List;

public class Properties {
    // Properties to set up host-domain client server
    public static final String HELLO_KEY = "sonar.example.hello";
    public static final String CATEGORY = "Properties Example";
    // Header
    public static final String X_ATLASSINA_TOKEN = "X-Atlassian-Token";
    public static final String NO_CHECK = "nocheck";
    public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
    public static final String TEXT_PLAIN = "text/plain";

    //HTTP THE REQUEST METHOD
    public static final String POST = "POST";
    public static final String HTTP_1 = "The server is thinking through the request.";
    public static final String HTTP_2 = "Success! The request was successfully completed and the server gave the browser" +
            "the expected response.";
    public static final String HTTP_3 = "Redirected somewhere else.";
    public static final String HTTP_4 = "Client errors: Page not found. The site or page couldn’t be reached.";
    public static final String HTTP_5 = "Server errors: Failure.\"";


    // Authorization
    public static final String AUTHORIZATION = "Authorization";
    public static final String SPACE = " ";
    public static final String BASIC = "Basic" + SPACE;
    public static final String HTTP = "http";
    public static final String DOUBLE_DOTS = ":";
    public static final String SLASH = "/";
    public static final String HOSTNAME_DESTINATION = "localhost"; //TODO dodac proerties z sonara by ustawiać ten parametr z ui
    public static final String PORT = "8090"; //TODO dodac proerties z sonara by ustawiać ten parametr z ui
    public static final String REST_API_CONTENT = "rest" + SLASH + "api" + SLASH + "content";
    public static final String PAGE_ID = "65610";//TODO dodac proerties z sonara by ustawiać ten parametr z ui
    public static final String CHILD_ATTACHMENT = "child" + SLASH + "attachment";
    // Request Body
    public static final String TEXT_BODY = "text/plain";
    public static final String MINOR_EDIT = "minorEdit";
    public static final String COMMENT = "comment";
    public static final String COMMENT_BODY = "Adding report file to the " + HOSTNAME_DESTINATION + "on Port: " +PORT;

    // BOOLEAN
    public static final String TRUE_TEXT = "true";
    public static final Boolean TRUE_VALUE = true;
    public static final String FALSE_TEXT = "false";
    public static final Boolean FALSE_VALUE = false;

    // Error MGMT
    public static final String FIlE = "The file";
    public static final String THE_FIlE = "file";
    public static final String DOES_NOT_EXIST = " does not exist.";
    public static final String UPLOAD_FILE_ATLASSIAN = "Occurred error during uploading file to the "
            + HOSTNAME_DESTINATION + " website of atlassian in pageId: " + PAGE_ID;

    private Properties() {
        // only statics
    }

    public static List<PropertyDefinition> getProperties() {

        return asList(
                PropertyDefinition.builder(HELLO_KEY)
                .name("Hello")
                .description("Say Hello")
                .defaultValue(String.valueOf(false))
                .category(CATEGORY)
                .build());
    }
}
