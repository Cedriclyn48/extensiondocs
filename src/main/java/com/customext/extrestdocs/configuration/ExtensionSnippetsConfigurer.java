package com.customext.extrestdocs.configuration;

import com.customext.extrestdocs.restdocs.snippets.PathSnippet;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.config.SnippetConfigurer;
import org.springframework.restdocs.snippet.Snippet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ExtensionSnippetsConfigurer extends
        SnippetConfigurer <ExtensionRestDocumentConfigurer, ExtensionSnippetsConfigurer>
        implements Filter {

    private List<Snippet> extensionSnippets = new ArrayList<>(Arrays.asList(new PathSnippet()));

    protected ExtensionSnippetsConfigurer(ExtensionRestDocumentConfigurer extensionRestDocumentConfigurer) {
        super(extensionRestDocumentConfigurer);
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext context) {
        return and().filter(requestSpec, responseSpec, context);
    }

    @Override
    public void apply(Map<String, Object> configuration, RestDocumentationContext context) {
        configuration.put("com.customext.extrestdocs.extsnippets", extensionSnippets);
        super.apply(configuration, context);

    }
}
