package com.customext.extrestdocs.configuration;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.springframework.restdocs.config.OperationPreprocessorsConfigurer;

public class ExtensionOperationPreprocessorsConfigurer extends
        OperationPreprocessorsConfigurer<ExtensionRestDocumentConfigurer, ExtensionOperationPreprocessorsConfigurer>
        implements Filter {

    protected ExtensionOperationPreprocessorsConfigurer(ExtensionRestDocumentConfigurer extensionRestDocumentConfigurer) {
        super(extensionRestDocumentConfigurer);
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext context) {
        return and().filter(requestSpec, responseSpec, context);
    }
}
