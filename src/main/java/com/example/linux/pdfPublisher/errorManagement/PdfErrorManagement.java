package com.example.linux.pdfPublisher.errorManagement;

import com.example.linux.pdfPublisher.settings.Base64Coder;
import com.example.linux.pdfPublisher.settings.DestinationProperties;
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
                .method(DestinationProperties.DESTINATION_POST, body)
                .addHeader(DestinationProperties.X_ATLASSINA_TOKEN, DestinationProperties.NO_CHECK)
                .addHeader(DestinationProperties.AUTHORIZATION, DestinationProperties.BASIC + DestinationProperties.SPACE +
                        Base64Coder.getEncodedString(
                                login,
                                password))
                .build();
    }

    public void checkHttpStatus(Response response) {
        switch (httpStatusCodeChecker(response.code())) {
            case 1:
                LOGGER.info(DestinationProperties.HTTP_1);
                break;
            case 2:
                LOGGER.info(DestinationProperties.HTTP_2);
                break;
            case 3:
                LOGGER.info(DestinationProperties.HTTP_3);
                break;
            case 4:
                LOGGER.info(DestinationProperties.HTTP_4);
                break;
            default:
            case 5:
                LOGGER.info(DestinationProperties.HTTP_5);
                break;
        }
    }
    private int httpStatusCodeChecker(int code) {
        return Integer.parseInt(Integer.toString(code).substring(0, 1));
    }
}
