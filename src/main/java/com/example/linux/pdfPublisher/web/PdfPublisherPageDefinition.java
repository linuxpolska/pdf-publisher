package com.example.linux.pdfPublisher.web;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;

import java.lang.module.Configuration;

import static org.sonar.api.web.page.Page.Qualifier.*;
import static org.sonar.api.web.page.Page.Scope.COMPONENT;

public class PdfPublisherPageDefinition implements PageDefinition {

    @Override
    public void define(Context context) {
        context
                .addPage(Page.builder("pdfpublisher/projectpage")
                        .setName("PDF Publisher Plugin Page")
                        .setAdmin(true) // Only admin can adjust properties on this page
                        .build());
    }
}
