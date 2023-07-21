package com.customext.extrestdocs.service;

import com.customext.extrestdocs.restdocs.snippets.DescriptionSnippet;
import com.customext.extrestdocs.restdocs.snippets.PathSnippet;
import com.customext.extrestdocs.restdocs.RestDocsUtils;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.TestInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Field;

import static com.customext.extrestdocs.restdocs.snippets.DescriptionSnippet.settingClassName;
import static com.customext.extrestdocs.restdocs.RestDocsUtils.*;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured3.RestAssuredRestDocumentation.documentationConfiguration;

@Aspect
@Component
public class RestDocsAdvisor {

    @Autowired
    RestDocsExtensionService restDocsExtensionService;

    @After("@annotation(com.customext.extrestdocs.annotation.RestDocsApply)")
    public void processCustomAnnotation(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        TestInfo testInfo = getTestInfo(args);

        restDocsExtensionService.createExtension(testInfo);
    }

    private TestInfo getTestInfo(Object[] args) {
        for (Object object : args) {
            if (object instanceof TestInfo) {
                return (TestInfo) object;
            }
        }
        throw new IllegalArgumentException("Extension : TestInfo Parameter Not Exist");
    }
}
