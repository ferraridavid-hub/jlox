package com.primeur.scanner;

import com.primeur.Lox;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class ScannerTest {

    @Test
    void testValidBlockComment() throws IOException {
        String path = "src/test/resources/scanner/valid/valid_block_comment.lox";
        Lox.main(new String[]{path});
    }

    @Test
    void testValidNestedBlockComment() throws IOException {
        String path = "src/test/resources/scanner/valid/valid_nested_block_comment.lox";
        Lox.main(new String[]{path});
    }

    @Test
    void testInvalidNestedBlockComment() throws IOException {
        String path = "src/test/resources/scanner/invalid/invalid_nested_block_comment.lox";
        Lox.main(new String[]{path});
    }
}