package com.customext.extrestdocs.configuration;

import com.customext.extrestdocs.service.RestDocsExtensionService;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit.jupiter.SpringExtension;

//@Component
public class ExtensionApplyConfig implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        ApplicationContext applicationContext = SpringExtension.getApplicationContext(context);
        Object mapping = applicationContext.getBean("requestMappingHandlerMapping");
        System.out.println(mapping);
//        restDocsExtensionService.createExtension(context);
    }
}
