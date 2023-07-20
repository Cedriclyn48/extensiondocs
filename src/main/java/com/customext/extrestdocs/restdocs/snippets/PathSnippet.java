package com.customext.extrestdocs.restdocs.snippets;

import com.customext.extrestdocs.service.RestDocsExtensionService;
import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;
import org.springframework.restdocs.snippet.WriterResolver;
import org.springframework.restdocs.templates.TemplateEngine;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.customext.extrestdocs.service.RestDocsExtensionService.*;


public class PathSnippet extends TemplatedSnippet {

    private String name = "path";
    
    public PathSnippet() {
        super("path", Collections.singletonMap("path", path));
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        Map<String, Object> map = new HashMap<>();
        map.put("path", path);
        return map;
    }

    @Override
    public void document(Operation operation) throws IOException {
        RestDocumentationContext context = (RestDocumentationContext) operation.getAttributes()
                .get(RestDocumentationContext.class.getName());
        WriterResolver writerResolver = (WriterResolver) operation.getAttributes().get(WriterResolver.class.getName());
        try (Writer writer = writerResolver.resolve(operation.getName(), name, context)) {
            Map<String, Object> model = createModel(operation);
            TemplateEngine templateEngine = (TemplateEngine) operation.getAttributes()
                    .get(TemplateEngine.class.getName());
            writer.append(templateEngine.compileTemplate(name).render(model));
        }
    }
}
