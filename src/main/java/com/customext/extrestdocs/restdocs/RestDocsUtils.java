package com.customext.extrestdocs.restdocs;

import com.customext.exception.NoURLMappingException;
import com.customext.extrestdocs.restdocs.preprocessor.AuthorizationOperationPreprocessor;
import com.customext.extrestdocs.restdocs.preprocessor.BinaryOperationPreprocessor;
import io.restassured.specification.FilterableRequestSpecification;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.util.Map;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;

/***
 * OperationPreprocessor register
 */
public interface RestDocsUtils {
    static OperationRequestPreprocessor getDocumentRequest() {
        return preprocessRequest(
                modifyUris()
                        .scheme("https")
                        .host("aivoicestudio.ai")
                        .removePort(),
                prettyPrint(),
                new BinaryOperationPreprocessor(),
                new AuthorizationOperationPreprocessor()
        );
    }

    static OperationResponsePreprocessor getDocumentResponse() {
        return preprocessResponse(
                prettyPrint(),
                new BinaryOperationPreprocessor()
        );
    }


    static String getOriginalPath(FilterableRequestSpecification requestSpec, RequestMappingHandlerMapping mapping) throws NoURLMappingException {
        String requestPath = requestSpec.getUserDefinedPath();
        String method = requestSpec.getMethod();

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = mapping.getHandlerMethods();
        for (RequestMappingInfo mappingInfo : handlerMethods.keySet()) {
            RequestMethod requestMethod = mappingInfo.getMethodsCondition().getMethods().stream().findFirst().orElse(null);
            if (mappingInfo.getPatternsCondition().getMatchingPatterns(requestPath).size() != 0
                    && requestMethod.toString().equals(method)) {
                return mappingInfo.getPatternsCondition().getPatterns().stream().findFirst().orElse(null);
            }
        }
        throw new NoURLMappingException();
    }


    static String getDisplayName(TestInfo testInfo) {
        Annotation[] a = testInfo.getTestMethod().get().getDeclaredAnnotations();
        for (Annotation annotation : a) {
            if (annotation.annotationType().getName().equals("org.junit.jupiter.api.DisplayName")) {
                return annotation.toString().split("\"")[1];
            }
        }
        return testInfo.getDisplayName();
    }

    static String getDisplayName(ExtensionContext context) {
        Annotation[] a = context.getTestMethod().get().getDeclaredAnnotations();
        for (Annotation annotation : a) {
            if (annotation.annotationType().getName().equals("org.junit.jupiter.api.DisplayName")) {
                return annotation.toString().split("\"")[1];
            }
        }
        return context.getDisplayName();
    }
}
