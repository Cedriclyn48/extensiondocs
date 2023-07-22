package com.customext.extrestdocs.configuration;

import com.customext.extrestdocs.annotation.RestDocsApply;
import com.customext.extrestdocs.service.RestDocsExtensionService;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@Component
public class ExtensionApplyConfig implements BeforeEachCallback {

    public static int port;

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        getPort(context);
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        RestDocsExtensionService service = (RestDocsExtensionService) applicationContext.getBean("restDocsExtensionService");
        service.createExtension(context);
    }

    private static void getPort(ExtensionContext context) {
        Class<?> testClass = context.getRequiredTestClass();
        RestDocsApply restDocsApply = testClass.getAnnotation(RestDocsApply.class);
        port = restDocsApply.port();
    }


}
