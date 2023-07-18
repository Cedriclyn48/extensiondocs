package com.customext.extrestdocs.service;

import com.customext.extrestdocs.annotation.RestDocsApply;
import org.junit.jupiter.api.TestInfo;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.stereotype.Service;

@Service
public class RestDocsExtensionService {

    @RestDocsApply
    public void createExtension(RestDocumentationContextProvider provider, TestInfo testInfo) {

    }
}