package com.example.linux.pdfPublisheru;

import com.example.linux.pdfPublisheru.hooks.PostJobInScanner;
import com.example.linux.pdfPublisheru.settings.Properties;
import com.example.linux.pdfPublisheru.web.PdfPublisherPageDefinition;
import org.sonar.api.Plugin;

import java.util.Collections;

public class PdfPlugin implements Plugin {

    @Override
    public void define(Plugin.Context context) {
        // tutorial on hooks
        context.addExtensions(Collections.singleton(PostJobInScanner.class));
//                , DisplayQualityGateStatus.class);


        // tutorial on settings
        context
                .addExtensions(Properties.getProperties());
//                .addExtension(SayHelloFromScanner.class);

        // tutorial on web extensions
        context.addExtension(PdfPublisherPageDefinition.class);
    }
}
