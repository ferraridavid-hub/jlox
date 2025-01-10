package com.primeur.lexer;

import com.primeur.Lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {
    private final String source;
    private final List<Token> tokenList = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("break", TokenType.BREAK);
        keywords.put("class", TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("false", TokenType.FALSE);
        keywords.put("for", TokenType.FOR);
        keywords.put("fun", TokenType.FUN);
        keywords.put("if", TokenType.IF);
        keywords.put("nil", TokenType.NIL);
        keywords.put("or", TokenType.OR);
        keywords.put("print", TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("true", TokenType.TRUE);
        keywords.put("var", TokenType.VAR);
        keywords.put("while", TokenType.WHILE);
    }

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        while (!isAtEnd()) {
            // Here we are at the beginning of the next lexeme
            // current points to the first unconsumed character in the source string
            start = current;
            scanToken();
        }

        tokenList.add(new Token(TokenType.EOF, "", null, line));
        return tokenList;
    }

    private void scanToken() {
        char c = advance();
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;
            case '?':
                addToken(TokenType.QUESTION);
                break;
            case ':':
                addToken(TokenType.COLON);
                break;
            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;
            case '/':
                if (match('/')) {
                    // A comment goes until the end of the line
                    while (!isAtEnd() && peek() != '\n') {
                        advance();
                    }
                } else if (match('*')) {
                    multiLineComment();
                } else {
                    addToken(TokenType.SLASH);
                }
                break;
            case ' ':
            case '\r':
            case '\t':
                // Ignoring whitespace.
                break;
            case '\n':
                line++;
                break;
            case '"':
                string();
                break;
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                number();
                break;
            default:
                if (isAlpha(c)) {
                    identifier();
                } else {
                    Lox.error(line, "Scanner","Unexpected character.");
                }
                break;
        }
    }

    private void multiLineComment() {
        while((peek() != '*' || peekNext() != '/') && !isAtEnd()) {

            // Nested block comment
            if (peek() == '/' && peekNext() == '*') {
                advance();
                advance();
                multiLineComment();
            } else {
                if (peek() == '\n') {
                    line++;
                }
                advance();
            }
        }

        if (isAtEnd()) {
            Lox.error(line, "Scanner", "Unterminated block comment.");
            return;
        }

        // Consume the two closing characters */
        advance();
        advance();
    }

    private void identifier() {
        while (isAlphaNumeric(peek())) {
            advance();
        }

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) {
            type = TokenType.IDENTIFIER;
        }
        addToken(type);
    }

    private boolean isAlphaNumeric(char c) {
        return isDigit(c) || isAlpha(c);
    }

    private boolean isAlpha (char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                (c == '_');
    }

    private void number() {
        while (isDigit(peek())) {
            advance();
        }

        // Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            do {
                advance();
            } while (isDigit(peek()));
        }

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void string() {
        while (!isAtEnd() && peek() != '"') {
            if (peek() == '\n') {
                line++;
            }
            advance();
        }

        // The closing " was not found
        if (isAtEnd()) {
            Lox.error(line, "Scanner", "Unterminated string.");
            return;
        }

        String value = source.substring(start + 1, current);
        addToken(TokenType.STRING, value);
        advance();
    }

    private char peek() {
        if (isAtEnd()) {
            return '\0';
        }
        return source.charAt(current);
    }

    private char peekNext() {
        if (current + 1 >= source.length()) {
            return '\0';
        }
        return source.charAt(current + 1);
    }

    private char advance() {
        return source.charAt(current++);
    }

    private boolean match(char expected) {
        if (isAtEnd()) {
            return false;
        }
        if (peek() != expected) {
            return false;
        }
        advance();
        return true;
    }

    private void addToken(TokenType tokenType) {
        addToken(tokenType, null);
    }

    private void addToken(TokenType tokenType, Object value) {
        tokenList.add(new Token(tokenType, source.substring(start, current), value, line));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }
}
