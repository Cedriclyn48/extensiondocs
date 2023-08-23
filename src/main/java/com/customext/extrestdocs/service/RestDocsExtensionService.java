package com.customext.extrestdocs.service;

import com.customext.exception.NoURLMappingException;
import com.customext.extrestdocs.restdocs.RestDocsUtils;
import com.customext.extrestdocs.restdocs.snippets.DescriptionSnippet;
import com.customext.extrestdocs.restdocs.snippets.PathSnippet;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.restdocs.ManualRestDocumentation;
import org.springframework.restdocs.restassured3.RestAssuredSnippetConfigurer;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;

import static com.customext.extrestdocs.restdocs.RestDocsUtils.*;
import static com.customext.extrestdocs.restdocs.snippets.DescriptionSnippet.settingClassName;
import static com.customext.extrestdocs.templates.RestDocsExtensionTemplateResourceResolver.extensionTemplateEngine;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@Service
public class RestDocsExtensionService {

    public static int port;
    public static String path = "a";
    private static ManualRestDocumentation docsProvider = new ManualRestDocumentation();

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public RestDocsExtensionService(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }


    public RequestSpecification createExtension(ExtensionContext context) throws NoSuchFieldException, IllegalAccessException {
        return spec(context);
    }

    public static String getPath() {
        return path;
    }

    private RequestSpecification spec(ExtensionContext context) throws NoSuchFieldException, IllegalAccessException {
        docsProviderProcess(context);
        applyRestAssuredPort();
        return createRequestSpecification(context);
    }

    private RequestSpecification createRequestSpecification(ExtensionContext context) throws NoSuchFieldException, IllegalAccessException {
        Field requestSpecification = RestAssured.class.getField("requestSpecification");
        // Neccessary to detach each test classes.
        requestSpecification.set(null, null);

        // Set Each RequestSpecification
        RequestSpecification spec = getRequestSpecification(context);
        requestSpecification.set(null, spec);
        return spec;
    }

    private void applyRestAssuredPort() throws NoSuchFieldException, IllegalAccessException {
        Field assuredPort = RestAssured.class.getField("port");
        assuredPort.set(null, port);
    }

    private void docsProviderProcess(ExtensionContext context) {
        docsProvider.afterTest();
        docsProvider.beforeTest(context.getTestClass().get(), context.getTestMethod().get().getName());
    }

    private RequestSpecification getRequestSpecification(ExtensionContext context) {
        return new RequestSpecBuilder()
                .addFilter(getPathFilter())
                .addFilter(getExtensionCongifurer(context))
                .addFilter(getDocumentSetting(context))
                .build();
    }

    private RestDocumentationFilter getDocumentSetting(ExtensionContext context) {
        return document(settingClassName(context.getTestClass().get().toString()) + "/{method-name}",
                getDocumentRequest(), getDocumentResponse());
    }

    private RestAssuredSnippetConfigurer getExtensionCongifurer(ExtensionContext context) {
        return documentationConfiguration(docsProvider)
                .templateEngine(extensionTemplateEngine())
                .snippets()
                .withAdditionalDefaults(new DescriptionSnippet(getDisplayName(context))
                        , new PathSnippet());
    }

    private Filter getPathFilter() {
        return (requestSpec, responseSpec, ctx) ->
        {
            try {
                return pathFilter(requestSpec, responseSpec, ctx, requestMappingHandlerMapping);
            } catch (NoURLMappingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    private Response pathFilter(FilterableRequestSpecification requestSpec,
                                FilterableResponseSpecification responseSpec,
                                FilterContext ctx,
                                RequestMappingHandlerMapping mapping) throws NoURLMappingException {
        path = RestDocsUtils.getOriginalPath(requestSpec, mapping);
        return ctx.next(requestSpec, responseSpec);
    }


}
