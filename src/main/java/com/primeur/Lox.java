package com.primeur;

import com.primeur.interpreter.Interpreter;
import com.primeur.interpreter.RuntimeError;
import com.primeur.lexer.Scanner;
import com.primeur.lexer.Token;
import com.primeur.lexer.TokenType;
import com.primeur.parser.Parser;
import com.primeur.parser.ast.Stmt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {

    private static final Interpreter interpreter = new Interpreter();

    private static boolean hadError = false;
    private static boolean hadRuntimeError = false;

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.err.println("usage: jlox [script]");
            System.exit(64); // value of ER_USAGE defined in UNIX sysexits.h
        } else if (args.length == 1) {
            runFile(args[0]);
        } else {
            runPrompt();
        }
    }

    private static void runFile(String path) throws IOException {
        run(Files.readString(Paths.get(path), Charset.defaultCharset()));

        if (hadError) {
            System.exit(65); // EX_DATAERR
        }
        if (hadRuntimeError) {
            System.exit(70);    // EX_SOFTWARE (internal software error)
        }
    }

    private static void runPrompt() throws IOException {
        InputStreamReader input = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input);

        while(true) {
            String line = reader.readLine();
            if (line == null) {
                break;
            }
            run(line);
            hadError = false;
        }
    }

    private static void run(String source) {
        Scanner scanner = new Scanner(source);
        List<Token> tokenList = scanner.scanTokens();
        Parser parser = new Parser(tokenList);
        List<Stmt> statements = parser.parse();

        if (hadError) {
            return;
        }

        interpreter.interpret(statements);

    }

    public static void error (int line, String message) {
        report(line, "", message);
        hadError = true;
    }

    public static void error(Token token , String message) {
        if (token.getType() == TokenType.EOF) {
            report(token.getLine(), " at end", message);
        } else {
            report(token.getLine(), " at '" + token.getLexeme() + "'", message);
        }
        hadError = true;
    }

    public static void runtimeError(RuntimeError error) {
        Token token = error.getToken();
        String message = error.getMessage();
        report(token.getLine(), " at '" + token.getLexeme() + "'", message);
        hadRuntimeError = true;
    }

    private static void report(int line, String where, String message) {
        System.err.println("[line " + line + "] Error" + where + ": " + message);
    }
}