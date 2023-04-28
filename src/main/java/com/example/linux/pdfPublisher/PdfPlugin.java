package com.example.linux.pdfPublisher;

import com.example.linux.pdfPublisher.hooks.PostProjectAnalysis;
import com.example.linux.pdfPublisher.settings.PdfPublisherProperties;
import org.sonar.api.Plugin;

import java.util.Collections;

public class PdfPlugin implements Plugin {

    @Override
    public void define(Plugin.Context context) {
        // tutorial on hooks
        context.addExtensions(Collections.singleton(PostProjectAnalysis.class));
        // tutorial on settings
        context.addExtensions(PdfPublisherProperties.getProperties());
    }
}
