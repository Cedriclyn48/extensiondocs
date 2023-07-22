package com.customext.extrestdocs.templates;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.restdocs.templates.TemplateFormat;
import org.springframework.restdocs.templates.TemplateFormats;
import org.springframework.restdocs.templates.TemplateResourceResolver;
import org.springframework.restdocs.templates.mustache.MustacheTemplateEngine;

/***
 * To register and Auto Detect custom snippets path
 * extends StandardTemplateResourceResolver By adding custom path
 */
public class RestDocsExtensionTemplateResourceResolver implements TemplateResourceResolver {
    private final TemplateFormat templateFormat;

    public RestDocsExtensionTemplateResourceResolver() {
        this.templateFormat = TemplateFormats.asciidoctor();
    }

    @Override
    public Resource resolveTemplateResource(String name) {
        Resource formatSpecificCustomTemplate = getFormatSpecificCustomTemplate(name);
        if (formatSpecificCustomTemplate.exists()) {
            return formatSpecificCustomTemplate;
        }
        Resource customTemplate = getCustomTemplate(name);
        if (customTemplate.exists()) {
            return customTemplate;
        }
        Resource localCustomTemplate = getLocalCustomTemplate(name);
        if (localCustomTemplate.exists()) {
            return localCustomTemplate;
        }
        Resource defaultTemplate = getDefaultTemplate(name);
        if (defaultTemplate.exists()) {
            return defaultTemplate;
        }
        throw new IllegalStateException("Template named '" + name + "' could not be resolved");
    }

    private Resource getFormatSpecificCustomTemplate(String name) {
        return new ClassPathResource(String.format("org/springframework/restdocs/templates/%s/%s.snippet",
                this.templateFormat.getId(), name));
    }

    private Resource getCustomTemplate(String name) {
        return new ClassPathResource(String.format("com/customext/extrestdocs/templates/templates/%s.snippet", name));
    }

    private Resource getLocalCustomTemplate(String name) {
        return new ClassPathResource(String.format("org/springframework/restdocs/templates/%s.snippet", name));
    }

    private Resource getDefaultTemplate(String name) {
        return new ClassPathResource(String.format("org/springframework/restdocs/templates/%s/default-%s.snippet",
                this.templateFormat.getId(), name));
    }

    public static MustacheTemplateEngine extensionTemplateEngine() {
        return new MustacheTemplateEngine(new RestDocsExtensionTemplateResourceResolver());
    }
}
