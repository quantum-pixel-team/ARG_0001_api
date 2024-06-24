package com.quantum_pixel.arg.utilities;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TemplateUtilsTest {

    @Test
    void testInjectVariables_BasicReplacement() {

        // given
        String template = "Hello, {{ name }}!";
        Map<String, String> placeholders = Map.of("name", "John");

        // when
        String result = TemplateUtils.injectVariables(template, placeholders);

        // then
        assertEquals("Hello, John!", result);
    }

    @Test
    void testInjectVariables_MissingPlaceholder() {

        // given
        String template = "Hello, {{ name }}! How are you, {{ name }}?";
        Map<String, String> placeholders = Map.of("name", "John");

        // when
        String result = TemplateUtils.injectVariables(template, placeholders);

        // then
        assertEquals("Hello, John! How are you, John?", result);
    }


    @Test
    void testInjectVariables_MultipleReplacements() {

        // given
        String template = "Hello, {{ name }}! Welcome to {{ place }}.";
        Map<String, String> placeholders = Map.of("name", "John", "place", "Wonderland");

        // when
        String result = TemplateUtils.injectVariables(template, placeholders);

        // then
        assertEquals("Hello, John! Welcome to Wonderland.", result);
    }

    @Test
    void testInjectVariables_NoPlaceholders() {

        // given
        String template = "Hello, World!";
        Map<String, String> placeholders = Map.of("name", "John");

        // when
        String result = TemplateUtils.injectVariables(template, placeholders);

        // then
        assertEquals("Hello, World!", result);
    }

    @Test
    void testInjectVariables_PlaceholdersWithExtraSpaces() {

        // given
        String template = "Hello, {{   name   }}! Welcome to {{ place }}.";
        Map<String, String> placeholders = Map.of("name", "John", "place", "Wonderland");

        // when
        String result = TemplateUtils.injectVariables(template, placeholders);

        // then
        assertEquals("Hello, John! Welcome to Wonderland.", result);
    }
}
