package com.quantum_pixel.arg.utilities;

import java.util.Map;

public class TemplateUtils {

    private TemplateUtils() {
    }

    public static String injectVariables(String sqlTemplate, Map<String, String> placeholder) {
        String temp = sqlTemplate;
        for (Map.Entry<String, String> entry : placeholder.entrySet()) {
            String regex = "\\{\\{( *" + entry.getKey() + " *)\\}\\}";
            String replacement = entry.getValue() == null ? "" : entry.getValue();
            temp = temp.replaceAll(regex, replacement);
        }
        return temp;
    }
}