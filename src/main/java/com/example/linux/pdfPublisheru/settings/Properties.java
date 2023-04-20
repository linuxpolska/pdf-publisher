package com.example.linux.pdfPublisheru.settings;

import org.sonar.api.config.PropertyDefinition;
import org.sonar.api.resources.Qualifiers;
import static java.util.Arrays.asList;

import java.util.List;

public class Properties {
    // Properties to set up host-domain client server
    public static final String HELLO_KEY = "sonar.example.hello";
    public static final String CATEGORY = "Properties Example";

    private Properties() {
        // only statics
    }

    public static List<PropertyDefinition> getProperties() {

        return asList(
                PropertyDefinition.builder(HELLO_KEY)
                .name("Hello")
                .description("Say Hello")
                .defaultValue(String.valueOf(false))
                .category(CATEGORY)
                .build());
    }
}
