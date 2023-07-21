package com.customext.extrestdocs.service;

import com.customext.extrestdocs.configuration.ExtensionRestDocumentConfigurer;
import com.customext.extrestdocs.restdocs.snippets.DescriptionSnippet;
import com.customext.extrestdocs.restdocs.snippets.PathSnippet;
import com.customext.extrestdocs.restdocs.RestDocsUtils;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static com.customext.extrestdocs.configuration.ExtensionRestDocumentConfigurer.extensionTemplateEngine;
import static com.customext.extrestdocs.restdocs.snippets.DescriptionSnippet.settingClassName;
import static com.customext.extrestdocs.restdocs.RestDocsUtils.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@Service
public class RestDocsExtensionService {

    public static String path = "a";
    private static ManualRestDocumentation docsProvider = new ManualRestDocumentation();

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public RestDocsExtensionService(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    public RequestSpecification createExtension(TestInfo testInfo, RequestMappingHandlerMapping mapping) {
        return spec(testInfo, mapping);
    }

    public static String getPath() {
        return path;
    }

    private RequestSpecification spec(TestInfo testInfo, RequestMappingHandlerMapping mapping) {
        docsProvider.afterTest();
        docsProvider.beforeTest(testInfo.getTestClass().getClass(), testInfo.getDisplayName());
        return new RequestSpecBuilder()
                .addFilter(
                        ((requestSpec, responseSpec, ctx) ->
                                pathFilter(requestSpec, responseSpec, ctx, requestMappingHandlerMapping))
                )
                .addFilter(documentationConfiguration(docsProvider)
                        .templateEngine(extensionTemplateEngine())
                        .snippets()
                        .withAdditionalDefaults(new DescriptionSnippet(getDisplayName(testInfo))
                                , new PathSnippet()))
                .addFilter(document(settingClassName(testInfo.getTestClass().get().toString())+"/{method-name}", getDocumentRequest(), getDocumentResponse()))
                .build();
    }

    private Response pathFilter(FilterableRequestSpecification requestSpec,
                                FilterableResponseSpecification responseSpec,
                                FilterContext ctx,
                                RequestMappingHandlerMapping mapping) {
        path = RestDocsUtils.getOriginalPath(requestSpec, mapping);
        return ctx.next(requestSpec, responseSpec);
    }
}
