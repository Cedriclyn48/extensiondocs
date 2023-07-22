package com.customext.extrestdocs.restdocs.preprocessor;

import org.springframework.http.MediaType;
import org.springframework.restdocs.operation.*;
import org.springframework.restdocs.operation.preprocess.OperationPreprocessor;
import org.springframework.restdocs.operation.preprocess.PrettyPrintingContentModifier;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/***
 * OperationPreprocessor : Binary Content Type
 * Change body of Binary Content Type
 * Because Binary data cannot be read
 */
public class BinaryOperationPreprocessor implements OperationPreprocessor {

    public BinaryOperationPreprocessor() {
    }

    @Override
    public OperationRequest preprocess(OperationRequest request) {
        if (isBinaryContentType(request)) {
            OperationRequestFactory factory = new OperationRequestFactory();
            if (request.getParts().isEmpty()) {
                return factory.createFrom(request, "- Body is Binary Data -".getBytes(StandardCharsets.UTF_8));
            }
            Collection<OperationRequestPart> requestPart = replaceBinaryDataFromRequestPart(request);

            return factory.create(request.getUri(), request.getMethod(), request.getContent(), request.getHeaders(), request.getParameters(), requestPart, request.getCookies());
        }
        return request;
    }

    @Override
    public OperationResponse preprocess(OperationResponse response) {
        if (isBinaryContentType(response)) {
            OperationResponseFactory factory = new OperationResponseFactory();
            return factory.createFrom(response, "- Content is Binary Data -".getBytes(StandardCharsets.UTF_8));
        }
        return response;
    }

    private Collection<OperationRequestPart> replaceBinaryDataFromRequestPart(OperationRequest request) {
        OperationRequestPartFactory factory = new OperationRequestPartFactory();
        Collection<OperationRequestPart> parts = request.getParts();
        List<OperationRequestPart> newParts = new ArrayList<>();

        for (OperationRequestPart part : parts) {
            byte[] content = preprocessContent(part);
            OperationRequestPart newRequestPart = factory.create(part.getName(), replaceFileName(part), content, part.getHeaders());
            newParts.add(newRequestPart);
        }
        return newParts;
    }

    private byte[] preprocessContent(OperationRequestPart part) {
        MediaType contentType = part.getHeaders().getContentType();
        String fileName = replaceFileName(part);
        if (isBinaryContentType(contentType)) {
            return ("- Content is Binary data of " + fileName + "\n").getBytes(StandardCharsets.UTF_8);
        }

        PrettyPrintingContentModifier prettyModifier = new PrettyPrintingContentModifier();
        return prettyModifier.modifyContent(part.getContent(), contentType);
    }

    private boolean isBinaryContentType(MediaType contentType) {
        return contentType.includes(MediaType.APPLICATION_OCTET_STREAM) || contentType.includes(MediaType.MULTIPART_FORM_DATA);
    }

    private Boolean isBinaryContentType(OperationRequest request) {
        MediaType contentType = request.getHeaders().getContentType();
        if (contentType != null && isBinaryContentType(contentType)) {
            return true;
        }
        return false;
    }

    private Boolean isBinaryContentType(OperationResponse response) {
        MediaType contentType = response.getHeaders().getContentType();
        if (contentType != null && isBinaryContentType(contentType)) {
            return true;
        }
        return false;
    }

    private String replaceFileName(OperationRequestPart part) {
        String fileName = part.getSubmittedFileName();
        if (fileName.equals("file")) {
            return "";
        }
        return fileName;
    }
}
