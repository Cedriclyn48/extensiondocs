package com.customext.extrestdocs.service;

import com.customext.extrestdocs.configuration.ExtensionRestDocumentConfigurer;
import com.customext.extrestdocs.restdocs.snippets.DescriptionSnippet;
import com.customext.extrestdocs.restdocs.snippets.PathSnippet;
import com.customext.extrestdocs.restdocs.RestDocsUtils;
import io.restassured.RestAssured;
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

import java.lang.reflect.Field;

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
    private final TestInfo testInfo;

    public RestDocsExtensionService(RequestMappingHandlerMapping requestMappingHandlerMapping, TestInfo testInfo) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
        this.testInfo = testInfo;
    }

    public RequestSpecification createExtension(TestInfo testInfo, RequestMappingHandlerMapping mapping) throws NoSuchFieldException, IllegalAccessException {
        return spec();
    }

    public RequestSpecification createExtension() throws NoSuchFieldException, IllegalAccessException {
        return spec();
    }

    public static String getPath() {
        return path;
    }

    private RequestSpecification spec() throws NoSuchFieldException, IllegalAccessException {
        docsProvider.afterTest();
        docsProvider.beforeTest(testInfo.getTestClass().get(), testInfo.getTestMethod().get().getName());
        RequestSpecification spec = getRequestSpecification();
        Field requestSpecification = RestAssured.class.getField("requestSpecification");
        requestSpecification.set(null, spec);
        return getRequestSpecification();
    }

    private RequestSpecification getRequestSpecification() {
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
                .addFilter(document(settingClassName(testInfo.getTestClass().get().toString()) + "/{method-name}", getDocumentRequest(), getDocumentResponse()))
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
