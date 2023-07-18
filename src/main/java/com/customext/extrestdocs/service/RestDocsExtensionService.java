package com.customext.extrestdocs.service;

import com.customext.extrestdocs.annotation.RestDocsApply;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.TestInfo;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.stereotype.Service;

@Service
public class RestDocsExtensionService {

    @RestDocsApply
    public RequestSpecification createExtension(RestDocumentationContextProvider provider, TestInfo testInfo) {
        return RestAssured.requestSpecification;
    }
}
