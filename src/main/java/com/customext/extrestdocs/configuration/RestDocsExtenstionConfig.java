package com.customext.extrestdocs.configuration;

import com.customext.extrestdocs.service.RestDocsExtensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/***
 * Use Auto Configuration Of Springframework
 * Autowired RequestMappingHandlerMapping for Snippets
 * And Register External Service to Bean by Auto Configuration
 * src/main/resources/META-INf/spring-factories -> config file
 */
@Configuration
public class RestDocsExtenstionConfig {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Bean
    public RestDocsExtensionService restDocsExtensionService() {
        return new RestDocsExtensionService(requestMappingHandlerMapping);
    }
}
