package com.example.linux.pdfPublisheru.hooks;

import com.example.linux.pdfPublisheru.controller.FileUploadController;
import com.example.linux.pdfPublisheru.settings.Base64Coder;
import com.example.linux.pdfPublisheru.settings.DestinationProperties;
import com.example.linux.pdfPublisheru.settings.SourceProperties;
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
    private SourceProperties sourceProperties = new SourceProperties();
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
        sourceProperties.setProjectName(Optional.ofNullable(
                context.getProjectAnalysis().getProject()).map(Object::toString).get());
        // Branch Name
        sourceProperties.setBranchName(Optional.ofNullable(
                context.getProjectAnalysis().getBranch()).map(Object::toString).get());
        // Hostname - Custom
        sourceProperties.setHostname(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(SourceProperties.SONAR_LINUXPOLSKA_HOSTNAME)).map(Object::toString).get());
        // Port - Custom
        sourceProperties.setPortSonarQube(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(SourceProperties.SONAR_LINUXPOLSKA_PORT)).map(Object::toString).get());
        // Login - Custom
        sourceProperties.setLoginSonarQube(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(SourceProperties.SONAR_LINUXPOLSKA_LOGIN)).map(Object::toString).get());
        // Password - Custom
        sourceProperties.setPasswordSonarQube(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(SourceProperties.SONAR_LINUXPOLSKA_PASSWORD)).map(Object::toString).get());

        // Destination section
        // Hostname - Custom
        destinationProperties.setHostnameSource(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(DestinationProperties.CONFKUENCE_LINUXPOLSKA_HOSTNAME)).map(Object::toString).get());
        // Port - Custom
        destinationProperties.setPortConfluence(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(DestinationProperties.CONFKUENCE_LINUXPOLSKA_PORT)).map(Object::toString).get());
        // Login - Custom
        destinationProperties.setLoginConfluence(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(DestinationProperties.CONFKUENCE_LINUXPOLSKA_LOGIN)).map(Object::toString).get());
        // Password - Custom
        destinationProperties.setPasswordConfluence(Optional.ofNullable(
                context.getProjectAnalysis().getScannerContext().getProperties()
                        .get(DestinationProperties.CONFKUENCE_LINUXPOLSKA_PASSWORD)).map(Object::toString).get());

        // Destination section

            try {
                OkHttpClient client = new OkHttpClient().newBuilder()
                        .build();
                MediaType mediaType = MediaType.parse(DestinationProperties.TEXT_PLAIN);
                RequestBody body = RequestBody.create(mediaType, "");
                Request request = new Request.Builder()
                        .url(new SourceProperties().urlSourceBuilder(
                                sourceProperties.getHostnameSource(),
                                sourceProperties.getPortSonarQube(),
                                sourceProperties.getProjectName(),
                                sourceProperties.getBranchName()))
                        .method(SourceProperties.GET, body)
                        .addHeader(SourceProperties.AUTHORIZATION,
                                SourceProperties.BASIC +
                                        SourceProperties.SPACE +
                                        Base64Coder.getEncodedString(
                                        sourceProperties.getLoginSonarQube(),
                                        sourceProperties.getPasswordSonarQube()
                                ))
                        .build();
                Response response = client.newCall(request).execute();

                FileUtils.writeByteArrayToFile(new File((
                        new SourceProperties().getProjectName() + SourceProperties.EXTENSION)), response.body().bytes());
                // Retrieve pdf file from response
                File file = new File((new SourceProperties().getProjectName() + SourceProperties.EXTENSION));
                if (!file.exists()) {
                    LOGGER.error("File does not exists! File name: "
                            + (new SourceProperties().getProjectName() + SourceProperties.EXTENSION));
                    throw new IOException("File does not exists!");
                }
                // Send a file to the fileUpload Controller
                FileUploadController fileUploadController = new FileUploadController();
                fileUploadController.handleFileUpload(file);
            } catch (NoSuchFileException noSuchFileException) {
                // TODO stworzyc obiekt zwracany ze jest blad ??????????????????????????????????
                LOGGER.error((new SourceProperties().getProjectName() + SourceProperties.EXTENSION) +
                        SourceProperties.DOES_NOT_EXIST);
                LOGGER.warn(noSuchFileException.getMessage());
                noSuchFileException.printStackTrace();
            } catch (Exception e) {
                LOGGER.error(SourceProperties.UPLOAD_FILE_SONARQUBE);
                LOGGER.warn(e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
