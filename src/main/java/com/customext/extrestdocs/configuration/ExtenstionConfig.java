package com.customext.extrestdocs.configuration;

import com.customext.extrestdocs.service.RestDocsExtensionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExtenstionConfig {
    @Bean
    public RestDocsExtensionService restDocsExtensionService() {
        return new RestDocsExtensionService();
    }


}
