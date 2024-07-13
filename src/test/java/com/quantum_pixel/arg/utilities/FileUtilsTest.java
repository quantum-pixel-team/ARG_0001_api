package com.quantum_pixel.arg.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
class FileUtilsTest {

    @Test
    void testExtractFileName() {
        assertEquals("test.txt", FileUtils.extractFileName("/test.txt"));
        assertEquals("test.txt", FileUtils.extractFileName("\\test.txt"));
        assertEquals("test.txt", FileUtils.extractFileName("test.txt"));
    }


    @Test
    void testReadResource_FileNotFound() {

        // When
        Executable executable = () -> FileUtils.readResource("/nonexistent.txt");
        // then
        FileNotFoundException exception = assertThrows(FileNotFoundException.class, executable);
        assertEquals("/nonexistent.txt", exception.getMessage());
    }
}