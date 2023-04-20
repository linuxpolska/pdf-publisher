package com.example.linux.pdfPublisheru.web;

import org.sonar.api.web.page.Context;
import org.sonar.api.web.page.Page;
import org.sonar.api.web.page.PageDefinition;

import static org.sonar.api.web.page.Page.Qualifier.SUB_VIEW;
import static org.sonar.api.web.page.Page.Qualifier.VIEW;
import static org.sonar.api.web.page.Page.Scope.COMPONENT;

public class PdfPublisherPageDefinition implements PageDefinition {

    @Override
    public void define(Context context) {
        context
                .addPage(Page.builder("pdf/global_page")
                        .setName("pdf publisher")
                        .build());
    }
}
