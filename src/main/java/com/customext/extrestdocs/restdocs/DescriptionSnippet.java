package com.customext.extrestdocs.restdocs;

import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.snippet.TemplatedSnippet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class DescriptionSnippet extends TemplatedSnippet {
    public DescriptionSnippet(String description, String className) {
        super("description", Collections.unmodifiableMap(convertToMap(description, className)));
    }

    public DescriptionSnippet(String description) {
        super("description", Collections.singletonMap("text", settingText(description)));
    }

    @Override
    protected Map<String, Object> createModel(Operation operation) {
        return operation.getAttributes();
    }

    private static HashMap<String, String> convertToMap(String description, String className) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("text", settingText(description));
        maps.put("className", settingClassName(className));
        return maps;
    }

    public static String settingClassName(String className) {
        String[] dotSplit = className.split("\\.");
        return dotSplit[dotSplit.length - 1].split("Acceptance")[0];
    }

    private static String settingText(String description) {
        return description.replace('_', ' ').replace("()", ".");
    }
}
