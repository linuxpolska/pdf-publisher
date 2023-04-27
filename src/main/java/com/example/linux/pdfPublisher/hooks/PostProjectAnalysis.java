package com.example.linux.pdfPublisher.hooks;

import com.example.linux.pdfPublisher.controller.FileUploadController;
import com.example.linux.pdfPublisher.settings.Base64Coder;
import com.example.linux.pdfPublisher.settings.PdfPublisherProperties;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.NoSuchElementException;
import java.util.Optional;

public class PostProjectAnalysis implements PostProjectAnalysisTask {
    private static final Logger LOGGER = Loggers.get(PostProjectAnalysis.class);
    private PdfPublisherProperties pdfPublisherProperties = new PdfPublisherProperties();

    @Override
    public String getDescription() {
        return PostProjectAnalysisTask.super.getDescription();
    }

    @Override
    public void finished(Context context) {
        LOGGER.info("Downloading an report of analysis has been submitted.");
        try {
            // Gettings values from user interface of plugin
            //Source Section
            // Project Name
            pdfPublisherProperties.setProjectSonarQubeName(Optional.ofNullable(
                    context.getProjectAnalysis().getProject()).map(Object::toString).get());
            // Branch Name
            pdfPublisherProperties.setBranchSonarQubeName(Optional.ofNullable(
                    context.getProjectAnalysis().getBranch()).map(Object::toString).get());
            // Hostname - Custom
            pdfPublisherProperties.setHostname(Optional.ofNullable(
                    context.getProjectAnalysis().getScannerContext().getProperties()
                            .get(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_HOSTNAME)).map(Object::toString).get());
            // Port - Custom
            pdfPublisherProperties.setPortSonarQube(Optional.ofNullable(
                    context.getProjectAnalysis().getScannerContext().getProperties()
                            .get(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_PORT)).map(Object::toString).get());
            // Login - Custom
            pdfPublisherProperties.setLoginSonarQube(Optional.ofNullable(
                    context.getProjectAnalysis().getScannerContext().getProperties()
                            .get(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_LOGIN)).map(Object::toString).get());
            // Password - Custom
            pdfPublisherProperties.setPasswordSonarQube(Optional.ofNullable(
                    context.getProjectAnalysis().getScannerContext().getProperties()
                            .get(PdfPublisherProperties.PDF_PUBLISHER_SONARQUBE_PASSWORD)).map(Object::toString).get());

            // Destination section
            // Hostname - Custom
            pdfPublisherProperties.setHostnameConfluence(Optional.ofNullable(
                    context.getProjectAnalysis().getScannerContext().getProperties()
                            .get(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_HOSTNAME)).map(Object::toString).get());
            // Port - Custom
            pdfPublisherProperties.setPortConfluence(Optional.ofNullable(
                    context.getProjectAnalysis().getScannerContext().getProperties()
                            .get(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_PORT)).map(Object::toString).get());
            // Login - Custom
            pdfPublisherProperties.setLoginConfluence(Optional.ofNullable(
                    context.getProjectAnalysis().getScannerContext().getProperties()
                            .get(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_LOGIN)).map(Object::toString).get());
            // Password - Custom
            pdfPublisherProperties.setPasswordConfluence(Optional.ofNullable(
                    context.getProjectAnalysis().getScannerContext().getProperties()
                            .get(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_PASSWORD)).map(Object::toString).get());
            // `PageId - Custom
            pdfPublisherProperties.setPageIdConfluence(Optional.ofNullable(
                    context.getProjectAnalysis().getScannerContext().getProperties()
                            .get(PdfPublisherProperties.PDF_PUBLISHER_CONFLUENCE_PAGEID)).map(Object::toString).get());

            // Destination section

            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse(PdfPublisherProperties.TEXT_PLAIN);
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(new PdfPublisherProperties().urlSourceBuilder(
                                pdfPublisherProperties.getHostnameSonarQubeSource(),
                                pdfPublisherProperties.getPortSonarQube(),
                                pdfPublisherProperties.getProjectSonarQubeName(),
                                pdfPublisherProperties.getBranchSonarQubeName()))
                        .method(PdfPublisherProperties.GET, body)
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
}

