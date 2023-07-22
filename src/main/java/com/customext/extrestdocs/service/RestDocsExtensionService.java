package com.customext.extrestdocs.service;

import com.customext.extrestdocs.restdocs.RestDocsUtils;
import com.customext.extrestdocs.restdocs.snippets.DescriptionSnippet;
import com.customext.extrestdocs.restdocs.snippets.PathSnippet;
import com.customext.extrestdocs.templates.ExtensionTemplateResourceResolver;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.templates.mustache.MustacheTemplateEngine;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;

import static com.customext.extrestdocs.restdocs.RestDocsUtils.*;
import static com.customext.extrestdocs.restdocs.snippets.DescriptionSnippet.settingClassName;
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

    public RequestSpecification createExtension(TestInfo testInfo) throws NoSuchFieldException, IllegalAccessException {
        return spec(testInfo);
    }

    public RequestSpecification createExtension(ExtensionContext context) throws NoSuchFieldException, IllegalAccessException {
        return spec(context);
    }

    public static String getPath() {
        return path;
    }

    @Value("${local.server.port}")
    int port;

    private RequestSpecification spec(TestInfo testInfo) throws NoSuchFieldException, IllegalAccessException {
        docsProvider.afterTest();
        docsProvider.beforeTest(testInfo.getTestClass().get(), testInfo.getTestMethod().get().getName());
        RestAssured.port = port;
        Field requestSpecification = RestAssured.class.getField("requestSpecification");
        requestSpecification.set(null, null);
        RequestSpecification spec = getRequestSpecification(testInfo);
        requestSpecification.set(null, spec);
        return spec;
    }

    private RequestSpecification spec(ExtensionContext context) throws NoSuchFieldException, IllegalAccessException {
        docsProvider.afterTest();
        docsProvider.beforeTest(context.getTestClass().get(), context.getTestMethod().get().getName());
        Field assuredPort = RestAssured.class.getField("port");
        assuredPort.set(null, port);
        Field requestSpecification = RestAssured.class.getField("requestSpecification");
        requestSpecification.set(null, null);
        RequestSpecification spec = getRequestSpecification(context);
        requestSpecification.set(null, spec);
        return spec;
    }

    private RequestSpecification getRequestSpecification(TestInfo testInfo) {
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

    private RequestSpecification getRequestSpecification(ExtensionContext context) {
        return new RequestSpecBuilder()
                .addFilter(
                        ((requestSpec, responseSpec, ctx) ->
                                pathFilter(requestSpec, responseSpec, ctx, requestMappingHandlerMapping))
                )
                .addFilter(documentationConfiguration(docsProvider)
                        .templateEngine(extensionTemplateEngine())
                        .snippets()
                        .withAdditionalDefaults(new DescriptionSnippet(getDisplayName(context))
                                , new PathSnippet()))
                .addFilter(document(settingClassName(context.getTestClass().get().toString()) + "/{method-name}", getDocumentRequest(), getDocumentResponse()))
                .build();
    }

    private Response pathFilter(FilterableRequestSpecification requestSpec,
                                FilterableResponseSpecification responseSpec,
                                FilterContext ctx,
                                RequestMappingHandlerMapping mapping) {
        path = RestDocsUtils.getOriginalPath(requestSpec, mapping);
        return ctx.next(requestSpec, responseSpec);
    }

    public static MustacheTemplateEngine extensionTemplateEngine() {
        return new MustacheTemplateEngine(new ExtensionTemplateResourceResolver());
    }
}
