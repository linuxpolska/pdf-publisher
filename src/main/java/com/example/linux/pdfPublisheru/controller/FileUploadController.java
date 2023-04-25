package com.example.linux.pdfPublisheru.controller;

import com.example.linux.pdfPublisheru.publisher.PdfReportPublisher;
import com.example.linux.pdfPublisheru.settings.DestinationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import okhttp3.RequestBody;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.NoSuchFileException;
import java.time.Clock;
import java.time.Instant;

@Controller
public class FileUploadController implements PostProjectAnalysisTask {
    // logger
    private static final Logger LOGGER = Loggers.get(FileUploadController.class);
    private Response response = null;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public FileUploadController() {
    }

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


    public Response handleFileUpload(File file) {
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

            PdfReportPublisher pdfReportPublisher = new PdfReportPublisher();
            Request request = pdfReportPublisher.publishPdfToPage(
                    DestinationProperties.urlDestinationBuilder(),
                    body);
            response = client.newCall(request).execute();
            System.out.println(response.body().toString());
            pdfReportPublisher.checkHttpStatus(response);

            LOGGER.warn(String.valueOf(response.code()));
        } catch (NoSuchFileException noSuchFileException) {
            // TODO stworzyc obiekt zwracany ze jest blad
            LOGGER.error(DestinationProperties.THE_FIlE + file.getName() + DestinationProperties.DOES_NOT_EXIST);
            LOGGER.warn(noSuchFileException.getMessage());
            noSuchFileException.printStackTrace();
        } catch (Exception e) {
            LOGGER.error(DestinationProperties.UPLOAD_FILE_ATLASSIAN);
            LOGGER.warn(e.getMessage());
            e.printStackTrace();
        }
        return response;
    }


    @PostMapping("/file") // TODO to be deleted
    public Response testUpload(
            @RequestParam("file") MultipartFile file) throws IOException {
        try {
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .build();
            RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart(DestinationProperties.FIlE, generateFileName("/D:/Grafiki/SpacesBools03.png"), // TODO - dodaj tutaj nazwe pliku
                            RequestBody.create(
                                    MediaType.parse(DestinationProperties.APPLICATION_OCTET_STREAM),
                                    new File("/D:/Grafiki/SpacesBools03.png"))) // TODO - put here body of file
//                                    file))
                    .addFormDataPart(DestinationProperties.MINOR_EDIT, DestinationProperties.TRUE_TEXT)
                    .addFormDataPart(DestinationProperties.COMMENT, null,
                            RequestBody.create(
                                    MediaType.parse(DestinationProperties.TEXT_PLAIN),
                                    DestinationProperties.COMMENT_BODY.getBytes()))
                    .build();

            PdfReportPublisher pdfReportPublisher = new PdfReportPublisher();
            Request request = pdfReportPublisher.publishPdfToPage(
                    DestinationProperties.urlDestinationBuilder(),
                    body);
            response = client.newCall(request).execute();
            System.out.println(response.body().toString());
            pdfReportPublisher.checkHttpStatus(response);

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
        return response; // TODO Zwraca 500 za kazdym razem -czy to je ważne
    }

    private String generateFileName(String fileName) {
        Instant currentTimeStamp = Clock.systemUTC().instant();
        return currentTimeStamp + "_" + fileName.substring(0, 0) + fileName.substring(0);
    }
}
