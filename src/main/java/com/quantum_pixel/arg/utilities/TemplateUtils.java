package com.quantum_pixel.arg.utilities;

import java.util.Map;

public final class TemplateUtils {


    public static String injectVariables(String sqlTemplate, Map<String, String> placeholder) {
        String temp = sqlTemplate;
        for (Map.Entry<String, String> entry : placeholder.entrySet()) {
            String regex = "\\{\\{( *" + entry.getKey() + " *)\\}\\}";
            String replacement = entry.getValue();
            temp = temp.replaceAll(regex, replacement);
        }
        return temp;
    }
}