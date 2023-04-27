package com.example.linux.pdfPublisher.errorManagement;

import com.example.linux.pdfPublisher.settings.Base64Coder;
import com.example.linux.pdfPublisher.settings.PdfPublisherProperties;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class PdfErrorManagement {
private static final Logger LOGGER = Loggers.get(PdfErrorManagement.class);
    public Request publishPdfToPage(String urlString, RequestBody body, String login, String password) {
        return new Request.Builder()
                .url(urlString.toString())
                .method(PdfPublisherProperties.CONFULENCE_POST, body)
                .addHeader(PdfPublisherProperties.X_ATLASSINA_TOKEN, PdfPublisherProperties.NO_CHECK)
                .addHeader(PdfPublisherProperties.AUTHORIZATION, PdfPublisherProperties.BASIC +
                        PdfPublisherProperties.SPACE + Base64Coder.getEncodedString(login, password))
                .build();
    }

    public void checkHttpStatus(Response response) {
        switch (httpStatusCodeChecker(response.code())) {
            case 1:
                LOGGER.info(PdfPublisherProperties.HTTP_1);
                break;
            case 2:
                LOGGER.info(PdfPublisherProperties.HTTP_2);
                break;
            case 3:
                LOGGER.info(PdfPublisherProperties.HTTP_3);
                break;
            case 4:
                LOGGER.info(PdfPublisherProperties.HTTP_4);
                break;
            default:
            case 5:
                LOGGER.info(PdfPublisherProperties.HTTP_5);
                break;
        }
    }
    private int httpStatusCodeChecker(int code) {
        return Integer.parseInt(Integer.toString(code).substring(0, 1));
    }
}
