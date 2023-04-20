package com.example.linux.pdfPublisheru.hooks;

import org.sonar.api.batch.postjob.PostJob;
import org.sonar.api.batch.postjob.PostJobContext;
import org.sonar.api.batch.postjob.PostJobDescriptor;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

public class PostJobInScanner implements PostJob {

    private static final Logger LOGGER = Loggers.get(PostJobInScanner.class);

    @Override
    public void describe(PostJobDescriptor descriptor) {
        descriptor.name("After scan");
    }

    @Override
    public void execute(PostJobContext context) {
        LOGGER.info("Something to do after the analysis report has been submitted");
    }
}
