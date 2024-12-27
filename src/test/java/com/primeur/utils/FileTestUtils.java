package com.primeur.utils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileTestUtils {
    public static String loadTestFile(String path) throws IOException {
        return Files.readString(Paths.get(path), Charset.defaultCharset());
    }
}
