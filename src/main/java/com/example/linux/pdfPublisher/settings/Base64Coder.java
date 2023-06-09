package com.example.linux.pdfPublisher.settings;

import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.Base64;

public class Base64Coder {
    private static final Logger LOGGER = Loggers.get(Base64Coder.class);

    public static String getEncodedString(String l, String p) {
        if (l == null || p == null) {
            LOGGER.error("Provided invalid authorization data! Please check the credentials. ");
            return null;
        }
        return Base64.getEncoder().encodeToString((l + ":" + p).getBytes());
    }
}
