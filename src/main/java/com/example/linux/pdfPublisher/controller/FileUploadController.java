package com.example.linux.pdfPublisher.controller;

import com.example.linux.pdfPublisher.errorManagement.PdfErrorManagement;
import com.example.linux.pdfPublisher.settings.DestinationProperties;
import okhttp3.*;
import okhttp3.RequestBody;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.time.Clock;
import java.time.Instant;

public class FileUploadController implements PostProjectAnalysisTask {
    // logger
    private static final Logger LOGGER = Loggers.get(FileUploadController.class);
    private Response response = null;

    public FileUploadController() {}

    @Override
    public void finished(ProjectAnalysis analysis) {
        QualityGate gate = analysis.getQualityGate();
        analysis.getScannerContext().getProperties().get(DestinationProperties.HELLO_KEY);
        if (gate != null) {
            Loggers.get(getClass()).info("Quality gate is " + gate.getStatus());
        }
        try {
            System.out.println("Test");
        } catch (Exception e) {
        }
    }


    public void handleFileUpload(File file, DestinationProperties destinationProperties) {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(DestinationProperties.FIlE, generateFileName(file.getName()),
                            RequestBody.create(MediaType.parse(DestinationProperties.APPLICATION_OCTET_STREAM),
                                    file))
                    .addFormDataPart(DestinationProperties.MINOR_EDIT, DestinationProperties.TRUE_TEXT)
                    .addFormDataPart(DestinationProperties.COMMENT, null,
                            RequestBody.create(
                                    MediaType.parse(DestinationProperties.TEXT_PLAIN),
                                    DestinationProperties.COMMENT_BODY.getBytes()))
                    .build();

            PdfErrorManagement pdfErrorManagement = new PdfErrorManagement();
            Request request = pdfErrorManagement.publishPdfToPage(
                    DestinationProperties.urlDestinationBuilder(
                            destinationProperties.getHostnameDestination(),
                            destinationProperties.getPortDestination(),
                            destinationProperties.getPageId()),
                    body,
                    destinationProperties.getLoginDestination(),
                    destinationProperties.getPasswordDestination());
            response = client.newCall(request).execute();
            System.out.println(response.body().toString());
            pdfErrorManagement.checkHttpStatus(response);

            LOGGER.warn(String.valueOf(response.code()));
        } catch (NoSuchFileException noSuchFileException) {
            LOGGER.error(DestinationProperties.THE_FIlE + file.getName() + DestinationProperties.DOES_NOT_EXIST);
            LOGGER.warn(noSuchFileException.getMessage());
            noSuchFileException.printStackTrace();
        } catch (Exception e) {
            LOGGER.error(DestinationProperties.UPLOAD_FILE_ATLASSIAN);
            LOGGER.warn(e.getMessage());
            e.printStackTrace();
        }
    }
    private String generateFileName(String fileName) {
        Instant currentTimeStamp = Clock.systemUTC().instant();
        return currentTimeStamp + "_" + fileName.substring(0, 0) + fileName.substring(0);
    }
}
