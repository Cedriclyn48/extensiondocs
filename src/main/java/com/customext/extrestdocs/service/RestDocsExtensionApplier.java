package com.customext.extrestdocs.service;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static com.customext.extrestdocs.service.RestDocsExtensionService.*;

/***
 * Apply RestDocs Extension Setting By BeforeEachCallback
 */
public class RestDocsExtensionApplier implements BeforeEachCallback {


    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);

        getPort(applicationContext);
        createExtension(context, applicationContext);
    }

    private void createExtension(ExtensionContext context, ApplicationContext applicationContext) throws NoSuchFieldException, IllegalAccessException {
        RestDocsExtensionService service = (RestDocsExtensionService) applicationContext.getBean("restDocsExtensionService");
        service.createExtension(context);
    }

    private static void getPort(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("local.server.port");
        port = Integer.parseInt(property);
    }


}
