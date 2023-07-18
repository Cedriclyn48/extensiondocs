package com.customext.extrestdocs.service;

import com.customext.extrestdocs.restdocs.DescriptionSnippet;
import com.customext.extrestdocs.restdocs.PathSnippet;
import com.customext.extrestdocs.restdocs.RestDocsUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.junit.jupiter.api.TestInfo;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;

import static com.customext.extrestdocs.restdocs.DescriptionSnippet.settingClassName;
import static com.customext.extrestdocs.restdocs.RestDocsUtils.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@Aspect
@Component
public class RestDocsAdvisor {

    public static String path = "a";
    private RequestMappingHandlerMapping mapping;

    public RestDocsAdvisor(RequestMappingHandlerMapping mapping) {
        this.mapping = mapping;
    }

    @Around("@annotation(com.customext.extrestdocs.annotation.RestDocsApply)")
//    @After("@annotation(com.customext.extrestdocs.annotation.RestDocsApply)")
    public Object processCustomAnnotation(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
//        Signature signature = proceedingJoinPoint.getSignature();
        Object[] args = proceedingJoinPoint.getArgs();
        RequestSpecification spec = addd(args);

        Field requestSpecification = RestAssured.class.getField("requestSpecification");
        requestSpecification.set(null, spec);

        return proceedingJoinPoint.proceed();
    }

    private RequestSpecification addd(Object[] args) {
        try {
            if (args[0] instanceof RestDocumentationContextProvider) {
                return spec((RestDocumentationContextProvider) args[0], (TestInfo) args[1], mapping);
            }
            if (args[0] instanceof TestInfo) {
                return spec((RestDocumentationContextProvider) args[1], (TestInfo) args[0], mapping);
            }
            throw new Exception("Extension Error");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
