package com.customext.extrestdocs.restdocs.preprocessor;

import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationRequestFactory;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;

import java.util.List;
import java.util.Map;

/***
 * OperationPreprocessor : Authorization
 * Change Request Information about Authorization Header
 * Inform that Authorization Header Bearer Token
 */
public class AuthorizationOperationPreprocessor implements OperationPreprocessor {

    public AuthorizationOperationPreprocessor() {
    }

    @Override
    public OperationRequest preprocess(OperationRequest request) {
        OperationRequestFactory factory = new OperationRequestFactory();
        return factory.createFrom(request, modifiedHeader(request.getHeaders()));
    }

    @Override
    public OperationResponse preprocess(OperationResponse response) {
        return response;
    }

    private HttpHeaders modifiedHeader(HttpHeaders headers) {
        HttpHeaders modified = new HttpHeaders();
        for (Map.Entry<String, List<String>> header : headers.entrySet()) {
            if (header.getKey().equals("Authorization")) {
                modified.add(header.getKey(), "{access-token}");
                continue;
            }
            for (String value : header.getValue()) {
                modified.add(header.getKey(), value);
            }
        }
        return modified;
    }
}
