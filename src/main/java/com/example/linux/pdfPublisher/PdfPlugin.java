package com.example.linux.pdfPublisher;

import com.example.linux.pdfPublisher.hooks.PostProjectAnalysis;
import com.example.linux.pdfPublisher.settings.PdfPublisherProperties;
import com.example.linux.pdfPublisher.web.PdfPublisherPageDefinition;
import org.sonar.api.Plugin;

import java.util.Collections;

public class PdfPlugin implements Plugin {

    @Override
    public void define(Plugin.Context context) {
        // tutorial on hooks
        context.addExtensions(Collections.singleton(PostProjectAnalysis.class));
        // tutorial on settings
        context.addExtensions(PdfPublisherProperties.getProperties());
        // tutorial on web extensions
        context.addExtension(PdfPublisherPageDefinition.class);
    }
}
