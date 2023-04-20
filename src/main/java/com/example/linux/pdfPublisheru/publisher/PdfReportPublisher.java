package com.example.linux.pdfPublisheru.publisher;

//import com.atlassian.confluence.core.ConfluenceActionSupport;
//import com.atlassian.confluence.pages.Attachment;
//import com.atlassian.confluence.user.AuthenticatedUserThreadLocal;
//import com.atlassian.confluence.user.ConfluenceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PdfReportPublisher {
//        extends ConfluenceActionSupport  {

    @Autowired
    private Environment environment;

//    implements ServletRequestAware
//    ConfluenceUser confluenceUser = AuthenticatedUserThreadLocal.get();
//    File file = new File("C:\\test.pdf");
//    Attachment attachment = new Attachment(
//            file.getName(),
//            "application/" + PropertiesUtils.DOCUMENT_TYPE,
//            file.getTotalSpace(),
//            "");
    
//    attachment.setCreator(confluenceUser);
//    attachment.setCreationDate(new Date());
//    attachment.setLastModificationDate(new Date());
//    this.attachmentManager.saveAttachment(attachment, null, new FileInputStream(file));
//        Page page = this.pageManager.getPage(6914058);
//    page.addAttachment(attachment);
}
