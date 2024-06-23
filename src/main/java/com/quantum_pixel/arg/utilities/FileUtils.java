package com.quantum_pixel.arg.utilities;

import lombok.SneakyThrows;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class FileUtils {

    private FileUtils (){}
    @SneakyThrows
    public static String readResource(String resource)  {
        String name = extractFileName(resource);
        InputStream resourceStream = FileUtils.class.getResourceAsStream("/" + name);
        if(resourceStream == null) {
            throw new FileNotFoundException(resource);
        }
        return new Scanner(resourceStream, StandardCharsets.UTF_8).useDelimiter("\\A").next();
    }



    private static String extractFileName(String fileName) {
        boolean startsWithSlash = fileName.startsWith("/") || fileName.startsWith("\\");
        if (startsWithSlash) {
            return fileName.substring(1);
        }
        return fileName;
    }

}