package com.example.linux.pdfPublisher.hooks;

import com.example.linux.pdfPublisher.controller.FileUploadController;
import com.example.linux.pdfPublisher.settings.Base64Coder;
import com.example.linux.pdfPublisher.settings.PdfPublisherProperties;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Arrays;
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
        try { // TODO wywalic port ustawienia i zostawic by ustawiali w hostname polu
            // Getting values from user interface of plugin
            // SonarQube Section
            pdfPublisherProperties.setProjectSonarQubeName(Optional.ofNullable(
                    context.getProjectAnalysis().getProject().getName()).map(Object::toString).get());
            // Branch Name
            LOGGER.info(String.valueOf(Optional.ofNullable(context.getProjectAnalysis().getBranch().get().getName())));
            pdfPublisherProperties.setBranchSonarQubeName(context.getProjectAnalysis().getBranch().get().getName().map(Object::toString).get());
            // Hostname - Custom
            pdfPublisherProperties.setHostnameSonarQube(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_HOSTNAME))
                            .get(),","));
            LOGGER.info("SonarQube Login "+ pdfPublisherProperties.getHostnameSonarQube());
            // Port - Custom
            pdfPublisherProperties.setPortSonarQube(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_PORT))
                            .get(),","));
            LOGGER.info("SonarQube Port "+ pdfPublisherProperties.getPortSonarQube());
            // Login - Custom
            pdfPublisherProperties.setLoginSonarQube(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_LOGIN))
                            .get(),","));
            LOGGER.info("SonarQube Login "+ !pdfPublisherProperties.getLoginSonarQube().isEmpty());
            // Password - Custom
            pdfPublisherProperties.setPasswordSonarQube(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_PASSWORD))
                            .get(),","));
            LOGGER.info("SonarQube Password "+ !pdfPublisherProperties.getPasswordSonarQube().isEmpty());

            // Destination section
            // Hostname - Custom
            pdfPublisherProperties.setHostnameConfluence(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_HOSTNAME))
                            .get(),","));
            LOGGER.info("Confluence Hostname "+ pdfPublisherProperties.getHostnameConfluence());
            // Port - Custom
            pdfPublisherProperties.setPortConfluence(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_PORT))
                            .get(),","));
            LOGGER.info("Confluence Port "+ pdfPublisherProperties.getPortConfluence());
            // Login - Custom
            pdfPublisherProperties.setLoginConfluence(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_LOGIN))
                            .get(),","));
            LOGGER.info("Confluence Login "+ !pdfPublisherProperties.getLoginConfluence().isEmpty());
            // Password - Custom
            pdfPublisherProperties.setPasswordConfluence(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_PASSWORD))
                            .get(),","));
            LOGGER.info("Confluence Hostname " + !pdfPublisherProperties.getPasswordConfluence().isEmpty());
            // `PageId - Custom
            pdfPublisherProperties.setPageIdConfluence(convertStringArrayToString(
                    Optional.ofNullable(configuration.getStringArray(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_PAGEID))
                            .get(),","));
            LOGGER.info("Confluence PageId "+ pdfPublisherProperties.getPageIdConfluence());

            // Destination section

            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse(PdfPublisherProperties.TEXT_PLAIN);
//                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(new PdfPublisherProperties().urlSourceBuilder(
                                pdfPublisherProperties.getHostnameSonarQubeSource(),
                                pdfPublisherProperties.getPortSonarQube(),
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

                FileUtils.writeByteArrayToFile(new File((
                        new PdfPublisherProperties().getProjectSonarQubeName() + PdfPublisherProperties.DOT_EXTENSION)), response.body().bytes());
                // Retrieve pdf file from response
                File file = new File((new PdfPublisherProperties().getProjectSonarQubeName() + PdfPublisherProperties.DOT_EXTENSION));
                if (!file.exists()) {
                    LOGGER.error("File does not exists! File name: "
                            + (new PdfPublisherProperties().getProjectSonarQubeName() + PdfPublisherProperties.DOT_EXTENSION));
                    throw new IOException("File does not exists!");
                }
                // Send a file to the fileUpload Controller
                FileUploadController fileUploadController = new FileUploadController();
                fileUploadController.handleFileUpload(file, pdfPublisherProperties);
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

    private static String convertStringArrayToString(String[] strArr, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String str : strArr)
            sb.append(str).append(delimiter);
        return sb.substring(0, sb.length() - 1);
    }
}

