package com.customext.extrestdocs.configuration;

import com.customext.extrestdocs.annotation.RestDocsApply;
import com.customext.extrestdocs.service.RestDocsExtensionService;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@Component
public class ExtensionApplyConfig implements BeforeEachCallback {

    public static int port;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        getPort(applicationContext);
        RestDocsExtensionService service = (RestDocsExtensionService) applicationContext.getBean("restDocsExtensionService");
        service.createExtension(context);
    }

    private static void getPort(ApplicationContext applicationContext) {
        Environment environment = applicationContext.getEnvironment();
        String property = environment.getProperty("local.server.port");
        port = Integer.parseInt(property);
    }


}
