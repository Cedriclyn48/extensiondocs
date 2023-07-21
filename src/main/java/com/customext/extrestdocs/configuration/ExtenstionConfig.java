package com.customext.extrestdocs.configuration;

import com.customext.extrestdocs.service.RestDocsExtensionService;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
//@TestConfiguration
public class ExtenstionConfig {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private TestInfo testInfo;

    @Bean
    public RestDocsExtensionService restDocsExtensionService() {
        return new RestDocsExtensionService(requestMappingHandlerMapping, testInfo);
    }

}
