package com.example.linux.pdfPublisher.hooks;

import com.example.linux.pdfPublisher.controller.FileUploadController;
import com.example.linux.pdfPublisher.settings.Base64Coder;
import com.example.linux.pdfPublisher.settings.DestinationProperties;
import com.example.linux.pdfPublisher.settings.PdfPublisherProperties;
import okhttp3.*;
import org.apache.commons.io.FileUtils;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.util.Optional;

public class PostProjectAnalysis implements PostProjectAnalysisTask {
    private static final Logger LOGGER = Loggers.get(PostProjectAnalysis.class);
    private PdfPublisherProperties pdfPublisherProperties = new PdfPublisherProperties();
    private DestinationProperties destinationProperties = new DestinationProperties();

    @Override
    public String getDescription() {
        return PostProjectAnalysisTask.super.getDescription();
    }

    @Override
    public void finished(Context context) {
        LOGGER.info("Downloading an report of analysis has been submitted.");
        // Gettings values from user interface of plugin
        //Source Section
        // Project Name
        pdfPublisherProperties.setProjectName(Optional.ofNullable(
                context.getProjectAnalysis().getProject()).map(Object::toString).get());
        // Branch Name
        pdfPublisherProperties.setBranchName(Optional.ofNullable(
                context.getProjectAnalysis().getBranch()).map(Object::toString).get());
        // Hostname - Custom
        pdfPublisherProperties.setHostname(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(PdfPublisherProperties.SONAR_LINUXPOLSKA_HOSTNAME)).map(Object::toString).get());
        // Port - Custom
        pdfPublisherProperties.setPortSonarQube(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(PdfPublisherProperties.SONAR_LINUXPOLSKA_PORT)).map(Object::toString).get());
        // Login - Custom
        pdfPublisherProperties.setLoginSonarQube(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(PdfPublisherProperties.SONAR_LINUXPOLSKA_LOGIN)).map(Object::toString).get());
        // Password - Custom
        pdfPublisherProperties.setPasswordSonarQube(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(PdfPublisherProperties.SONAR_LINUXPOLSKA_PASSWORD)).map(Object::toString).get());

        // Destination section
        // Hostname - Custom
        destinationProperties.setHostnameDestination(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(DestinationProperties.CONFLUENCE_LINUXPOLSKA_HOSTNAME)).map(Object::toString).get());
        // Port - Custom
        destinationProperties.setPortDestination(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(DestinationProperties.CONFLUENCE_LINUXPOLSKA_PORT)).map(Object::toString).get());
        // Login - Custom
        destinationProperties.setLoginDestination(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(DestinationProperties.CONFLUENCE_LINUXPOLSKA_LOGIN)).map(Object::toString).get());
        // Password - Custom
        destinationProperties.setPasswordDestination(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(DestinationProperties.CONFLUENCE_LINUXPOLSKA_PASSWORD)).map(Object::toString).get());
        // `PageId - Custom
        destinationProperties.setPageId(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(DestinationProperties.CONFLUENCE_LINUXPOLSKA_PAGEID)).map(Object::toString).get());

        // Destination section

        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            MediaType mediaType = MediaType.parse(DestinationProperties.TEXT_PLAIN);
            RequestBody body = RequestBody.create(mediaType, "");
            Request request = new Request.Builder()
                    .url(new PdfPublisherProperties().urlSourceBuilder(
                            pdfPublisherProperties.getHostnameSource(),
                            pdfPublisherProperties.getPortSonarQube(),
                            pdfPublisherProperties.getProjectName(),
                            pdfPublisherProperties.getBranchName()))
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
                    new PdfPublisherProperties().getProjectName() + PdfPublisherProperties.DOT_EXTENSION)), response.body().bytes());
            // Retrieve pdf file from response
            File file = new File((new PdfPublisherProperties().getProjectName() + PdfPublisherProperties.DOT_EXTENSION));
            if (!file.exists()) {
                LOGGER.error("File does not exists! File name: "
                        + (new PdfPublisherProperties().getProjectName() + PdfPublisherProperties.DOT_EXTENSION));
                throw new IOException("File does not exists!");
            }
            // Send a file to the fileUpload Controller
            FileUploadController fileUploadController = new FileUploadController();
            fileUploadController.handleFileUpload(file, destinationProperties);
        } catch (NoSuchFileException noSuchFileException) {
            LOGGER.error((pdfPublisherProperties.getProjectName() + PdfPublisherProperties.DOT_EXTENSION) +
                    PdfPublisherProperties.DOES_NOT_EXIST);
            LOGGER.warn(noSuchFileException.getMessage());
            noSuchFileException.printStackTrace();
        } catch (Exception e) {
            LOGGER.error(PdfPublisherProperties.UPLOAD_FILE_SONARQUBE);
            LOGGER.warn(e.getMessage());
            e.printStackTrace();
        }
    }
}

