package com.example.linux.pdfPublisheru;

import com.example.linux.pdfPublisheru.hooks.PostProjectAnalysis;
import com.example.linux.pdfPublisheru.settings.DestinationProperties;
import com.example.linux.pdfPublisheru.settings.SourceProperties;
import com.example.linux.pdfPublisheru.web.PdfPublisherPageDefinition;
import org.sonar.api.Plugin;

import java.util.Collections;

public class PdfPlugin implements Plugin {

    @Override
    public void define(Plugin.Context context) {
        // tutorial on hooks
        context.addExtensions(Collections.singleton(PostProjectAnalysis.class));
//                , DisplayQualityGateStatus.class);


        // tutorial on settings
        context
                .addExtensions(DestinationProperties.getProperties())
                .addExtensions(SourceProperties.getProperties());
//                .addExtension(SayHelloFromScanner.class);

        // tutorial on web extensions
        context.addExtension(PdfPublisherPageDefinition.class);
    }
}
