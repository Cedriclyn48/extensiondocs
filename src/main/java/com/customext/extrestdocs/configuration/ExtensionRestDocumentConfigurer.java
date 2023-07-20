package com.customext.extrestdocs.configuration;

import com.customext.extrestdocs.templates.ExtensionTemplateResourceResolver;
import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.config.AbstractConfigurer;
import org.springframework.restdocs.config.RestDocumentationConfigurer;
import org.springframework.restdocs.restassured3.RestDocumentationFilter;
import org.springframework.restdocs.templates.mustache.MustacheTemplateEngine;

import java.util.HashMap;
import java.util.Map;

public class ExtensionRestDocumentConfigurer extends
        RestDocumentationConfigurer <ExtensionSnippetsConfigurer, ExtensionOperationPreprocessorsConfigurer, ExtensionRestDocumentConfigurer>
        implements Filter {

    private final ExtensionSnippetsConfigurer snippetsConfigurer =
            new ExtensionSnippetsConfigurer(this);

    private final ExtensionOperationPreprocessorsConfigurer operationPreprocessorsConfigurer =
            new ExtensionOperationPreprocessorsConfigurer(this);
    private RestDocumentationContextProvider contextProvider;

    public ExtensionRestDocumentConfigurer(RestDocumentationContextProvider contextProvider) {
        this.contextProvider = contextProvider;
    }

    @Override
    public ExtensionSnippetsConfigurer snippets() {
        return this.snippetsConfigurer;
    }

    @Override
    public ExtensionOperationPreprocessorsConfigurer operationPreprocessors() {
        return this.operationPreprocessorsConfigurer;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec, FilterContext context) {
        RestDocumentationContext restContext = this.contextProvider.beforeOperation();
        context.setValue(RestDocumentationContext.class.getName(), context);
        Map<String, Object> configuration = new HashMap<>();
        context.setValue("com.customext.extrestdocs.configuration", configuration);
        apply(configuration, restContext);
        return context.next(requestSpec, responseSpec);
    }

    public static final class ExtensionTemplateEngineConfigurer extends AbstractConfigurer {
        public ExtensionTemplateEngineConfigurer() {
        }

        @Override
        public void apply(Map<String, Object> configuration, RestDocumentationContext context) {
            MustacheTemplateEngine engine = new MustacheTemplateEngine(new ExtensionTemplateResourceResolver());

            configuration.put("ExtensionTemplateEngine", engine);
        }
    }

    public static MustacheTemplateEngine extensionTemplateEngine() {
        return new MustacheTemplateEngine(new ExtensionTemplateResourceResolver());
    }


}
