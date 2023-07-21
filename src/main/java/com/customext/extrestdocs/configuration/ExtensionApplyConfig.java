package com.customext.extrestdocs.configuration;

import com.customext.extrestdocs.service.RestDocsExtensionService;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ExtensionApplyConfig implements BeforeEachCallback {

    @Autowired
    RestDocsExtensionService restDocsExtensionService;
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        restDocsExtensionService.createExtension(context);
    }
}
