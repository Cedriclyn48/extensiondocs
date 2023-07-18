package com.customext.extrestdocs.service;

import com.customext.extrestdocs.annotation.RestDocsApply;
import com.customext.extrestdocs.restdocs.DescriptionSnippet;
import com.customext.extrestdocs.restdocs.PathSnippet;
import com.customext.extrestdocs.restdocs.RestDocsUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.TestInfo;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import static com.customext.extrestdocs.restdocs.DescriptionSnippet.settingClassName;
import static com.customext.extrestdocs.restdocs.RestDocsUtils.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@Service
public class RestDocsExtensionService {

    public static String path = "a";

    public RequestSpecification createExtension(RestDocumentationContextProvider provider, TestInfo testInfo, RequestMappingHandlerMapping mapping) {
        return spec(provider, testInfo, mapping);
    }

    public static String getPath() {
        return path;
    }

    private RequestSpecification spec(RestDocumentationContextProvider docsProvider, TestInfo testInfo, RequestMappingHandlerMapping mapping) {
        return new RequestSpecBuilder()
                .addFilter(
                        ((requestSpec, responseSpec, ctx) ->
                        {
                            path = RestDocsUtils.getOriginalPath(requestSpec, mapping);
                            return ctx.next(requestSpec, responseSpec);
                        })
                )
                .addFilter(documentationConfiguration(docsProvider)
                        .snippets()
                        .withAdditionalDefaults(new DescriptionSnippet(getDisplayName(testInfo))
                                , new PathSnippet()))
                .addFilter(document(settingClassName(testInfo.getTestClass().get().toString())+"/{method-name}", getDocumentRequest(), getDocumentResponse()))
                .build();
    }
}
