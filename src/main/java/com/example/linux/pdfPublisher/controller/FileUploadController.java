package com.example.linux.pdfPublisher.controller;

import com.example.linux.pdfPublisher.errorManagement.PdfErrorManagement;
import com.example.linux.pdfPublisher.settings.PdfPublisherProperties;
import okhttp3.*;
import okhttp3.RequestBody;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;

public class FileUploadController implements PostProjectAnalysisTask {
    // logger
    private static final Logger LOGGER = Loggers.get(FileUploadController.class);

    public FileUploadController() {
        // Only for initialization
    }

    public void handleFileUpload(File file, PdfPublisherProperties destinationProperties) {
        Response response;
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(PdfPublisherProperties.FIlE, generateFileName(file.getName()),
                            RequestBody.create(MediaType.parse(PdfPublisherProperties.APPLICATION_OCTET_STREAM),
                                    file))
                    .addFormDataPart(PdfPublisherProperties.MINOR_EDIT, PdfPublisherProperties.TRUE_TEXT)
                    .build();

            PdfErrorManagement pdfErrorManagement = new PdfErrorManagement();
            Request request = pdfErrorManagement.publishPdfToPage(
                    PdfPublisherProperties.urlConfluenceBuilder(
                            destinationProperties.getHostnameConfluence(),
                            destinationProperties.getPageIdConfluence()),
                    body,
                    destinationProperties.getLoginConfluence(),
                    destinationProperties.getPasswordConfluence());
            response = client.newCall(request).execute();
            LOGGER.info(response.body().toString());
            pdfErrorManagement.checkHttpStatus(response);
            LOGGER.warn(String.valueOf(response.code()));
            response.close();
        } catch (NoSuchFileException noSuchFileException) {
            LOGGER.error(
                    PdfPublisherProperties.THE +
                            PdfPublisherProperties.SPACE +
                            PdfPublisherProperties.FIlE +
                            file.getName() +
                            PdfPublisherProperties.DOES_NOT_EXIST);
            LOGGER.warn(noSuchFileException.getMessage());
            noSuchFileException.printStackTrace();
        } catch (Exception e) {
            LOGGER.error(PdfPublisherProperties.UPLOAD_FILE_ATLASSIAN);
            LOGGER.warn(e.getMessage());
            e.printStackTrace();
        }
    }
    private String generateFileName(String fileName) {
        Timestamp ts = Timestamp.from(Instant.now());
        String s = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(ts);
        return s.toString().replaceAll("\\s", "_") + "_" + fileName.substring(0, 0) +
                fileName.substring(0);
    }
}
