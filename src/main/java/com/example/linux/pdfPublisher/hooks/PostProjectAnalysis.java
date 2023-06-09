package com.example.linux.pdfPublisher.hooks;

import com.example.linux.pdfPublisher.controller.FileUploadController;
import com.example.linux.pdfPublisher.errorManagement.PdfErrorManagement;
import com.example.linux.pdfPublisher.settings.Base64Coder;
import com.example.linux.pdfPublisher.settings.PdfPublisherProperties;
import okhttp3.*;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PostProjectAnalysis implements PostProjectAnalysisTask {
    private static final Logger LOGGER = Loggers.get(PostProjectAnalysis.class);
    private PdfPublisherProperties pdfPublisherProperties = new PdfPublisherProperties();
    private final Configuration configuration;

    public PostProjectAnalysis(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public String getDescription() {
        return PostProjectAnalysisTask.super.getDescription();
    }

    @Override
    public void finished(Context context) {
        LOGGER.info("Downloading an report of analysis has been submitted.");
        try {
            // Getting values from user interface of plugin
            // SonarQube Section
            pdfPublisherProperties.setProjectSonarQubeName(Optional.ofNullable(
                    context.getProjectAnalysis().getProject().getKey()).map(Object::toString).get());
            // Branch Name
            pdfPublisherProperties.setBranchSonarQubeName(context.getProjectAnalysis().getBranch().get().getName().map(Object::toString).get());


            // Hostname - Custom - Global
            pdfPublisherProperties.setHostnameSonarQube(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_HOSTNAME))
                            .get(), ","));
            // Login - Custom - Global
            pdfPublisherProperties.setLoginSonarQube(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_LOGIN))
                            .get(), ","));
            // Password - Custom - Global
            pdfPublisherProperties.setPasswordSonarQube(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_PASSWORD))
                            .get(), ","));
            // LOGGER.info("SonarQube Password " + !pdfPublisherProperties.getPasswordSonarQube().isEmpty());

            // Destination section
            // Hostname - Custom - Global
            pdfPublisherProperties.setHostnameConfluence(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_HOSTNAME))
                            .get(), ","));
            // LOGGER.info("Confluence Hostname " + pdfPublisherProperties.getHostnameConfluence());
            // Login - Custom - Global
            pdfPublisherProperties.setLoginConfluence(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_LOGIN))
                            .get(), ","));
            // LOGGER.info("Confluence Login " + !pdfPublisherProperties.getLoginConfluence().isEmpty());
            // Password - Custom - Global
            pdfPublisherProperties.setPasswordConfluence(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_PASSWORD))
                            .get(), ","));
            // LOGGER.info("Confluence Hostname " + !pdfPublisherProperties.getPasswordConfluence().isEmpty());
            // `PageId - Custom
            // http get
            pdfPublisherProperties.setPageIdConfluence(getPageIdFromApi());
            LOGGER.info("Confluence PageId " + pdfPublisherProperties.getPageIdConfluence());

            // Destination section
            try {
                File file = null;
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                Request request = new Request.Builder()
                        .url(new PdfPublisherProperties().urlSourceBuilder(
                                pdfPublisherProperties.getHostnameSonarQubeSource(),
                                pdfPublisherProperties.getProjectSonarQubeName(),
                                pdfPublisherProperties.getBranchSonarQubeName()))
                        .method(PdfPublisherProperties.GET, null)
                        .addHeader(PdfPublisherProperties.AUTHORIZATION,
                                PdfPublisherProperties.BASIC +
                                        PdfPublisherProperties.SPACE +
                                        Base64Coder.getEncodedString(
                                                pdfPublisherProperties.getLoginSonarQube(),
                                                pdfPublisherProperties.getPasswordSonarQube()
                                        ))
                        .build();
                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();
                int statusCode = response.code();
                if (body != null && statusCode < 400) {
                    LOGGER.info("Headers Content-Type is: " + response.headers().get("Content-Type"));
                    LOGGER.info("Body Content-Type is: " + response.body().contentType().toString());
                    if (body.contentType() != null &&
                            !body.contentType().toString().contains("multipart")) {
                        file = new File((pdfPublisherProperties.getProjectSonarQubeName()
                                + "_"
                                + pdfPublisherProperties.getBranchSonarQubeName()
                                + PdfPublisherProperties.DOT_EXTENSION));
                        // LOGGER.info("File name is: " + file.getName());
                        OutputStream outStream = new FileOutputStream(file);
                        try {
                            outStream.write(body.bytes()); // Buffer
                        } catch (Exception e) {
                            LOGGER.error("Buffer is empty" + e.getMessage());
                            e.printStackTrace();
                        }
                        IOUtils.closeQuietly(outStream);
                        if (!file.exists()) {
                            throw new IOException("File does not exists!");
                        }
                    } else {
                        LOGGER.warn("Contet-Type is wrong!");
                    }
                } else {
                    LOGGER.info("Response of " + pdfPublisherProperties.getHostnameSonarQube() + " is null!");
                    response.close();
                    return;
                }
                // Send a file to the fileUpload Controller
                FileUploadController fileUploadController = new FileUploadController();
                fileUploadController.handleFileUpload(file, pdfPublisherProperties);
                response.close();
            } catch (NoSuchFileException noSuchFileException) {
                LOGGER.error((pdfPublisherProperties.getProjectSonarQubeName() + PdfPublisherProperties.DOT_EXTENSION) +
                        PdfPublisherProperties.DOES_NOT_EXIST);
                LOGGER.warn(noSuchFileException.getMessage());
                noSuchFileException.printStackTrace();
            } catch (Exception e) {
                LOGGER.error(PdfPublisherProperties.UPLOAD_FILE_SONARQUBE);
                LOGGER.warn(e.getMessage());
                e.printStackTrace();
            }
        } catch (NoSuchElementException elementException) {
            LOGGER.error("No code report presents.");
            LOGGER.warn(elementException.getMessage());
            elementException.printStackTrace();
        }
    }

    private String getPageIdFromApi() {
        Response response;
        JSONObject json = null;
        String pageId = null;
        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        Request request = new Request.Builder()
                .url(new PdfPublisherProperties().urlSourcePageIDBuilder(
                        pdfPublisherProperties.getHostnameSonarQubeSource(),
                        pdfPublisherProperties.getProjectSonarQubeName()))
                .method(PdfPublisherProperties.GET, null)
                .addHeader(PdfPublisherProperties.AUTHORIZATION,
                        PdfPublisherProperties.BASIC +
                                PdfPublisherProperties.SPACE +
                                Base64Coder.getEncodedString(
                                        pdfPublisherProperties.getLoginSonarQube(),
                                        pdfPublisherProperties.getPasswordSonarQube()
                                ))
                .build();
        String url = new PdfPublisherProperties().urlSourcePageIDBuilder(
                pdfPublisherProperties.getHostnameSonarQubeSource(),
                pdfPublisherProperties.getProjectSonarQubeName());
        LOGGER.info("This is url for pageID:");
        LOGGER.info(url);
        try {
            response = client.newCall(request).execute();
            PdfErrorManagement pdfErrorManagement = new PdfErrorManagement();
            pdfErrorManagement.checkHttpStatus(response);
            try {
                json = new JSONObject(response.body().string());
                pageId = ((JSONObject) (((JSONArray) json.get("settings")).get(0))).get("value").toString();
            } catch (IOException io) {
                LOGGER.warn("JSON object is null.");
                io.printStackTrace();
            }
        } catch (IOException e) {
            LOGGER.warn("PageId is not defined! Please add correct value! If you want reports please add page id of" +
                    " confluence page");
            e.printStackTrace();
        }

        return pageId;
    }

    private static String convertStringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }
}

