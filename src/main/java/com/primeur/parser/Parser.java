package com.primeur.parser;

import com.primeur.Lox;
import com.primeur.lexer.Token;
import com.primeur.lexer.TokenType;
import com.primeur.parser.ast.*;
import com.primeur.parser.ast.error.ParseError;
import com.primeur.parser.ast.utils.ExpressionHandler;

import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public Expr parse() {
        try {
            return expression();
        } catch (ParseError e) {
            return null;
        }
    }

    private Expr expression() {
        return sequence();
    }

    private Expr sequence() {
        return leftAssociativeBinaryExpr(this::conditional, TokenType.COMMA);
    }

    private Expr conditional() {
        Expr left = equality();
        if (match(TokenType.QUESTION)) {
            Token leftOperand = previous();
            Expr middle = conditional();
            Token rightOperand = consume(TokenType.COLON, "Expected : in ternary conditional operator.");
            Expr right = conditional();
            left = new TernaryExpr(left, leftOperand, middle, rightOperand, right);
        }
        return left;

    }

    private Expr equality() {
        return leftAssociativeBinaryExpr(this::comparison, TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL);
    }

    private Expr comparison() {
        return leftAssociativeBinaryExpr(this::term, TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL);
    }

    private Expr term() {
        return leftAssociativeBinaryExpr(this::factor, TokenType.MINUS, TokenType.PLUS);
    }

    private Expr factor() {
        return leftAssociativeBinaryExpr(this::unary, TokenType.SLASH, TokenType.STAR);
    }

    private Expr unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new UnaryExpr(operator, right);
        }
        return primary();
    }

    private Expr primary() {
        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new LiteralExpr(previous().getLiteral());
        }

        if (match(TokenType.TRUE)) {
            return new LiteralExpr(true);
        }

        if (match(TokenType.FALSE)) {
            return new LiteralExpr(false);
        }

        if (match(TokenType.NIL)) {
            return new LiteralExpr(null);
        }

        if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expected ')' after expression.");
            return new GroupingExpr(expr);
        }

        throw error(peek(), "Expected expression.");
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }
        return peek().getType() == type;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }
        return previous();
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().getType() == TokenType.SEMICOLON) {
                return;
            }

            switch (peek().getType()) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                case RETURN:
                    return;
            }

            advance();
        }
    }

    private Expr leftAssociativeBinaryExpr(ExpressionHandler expressionHandler, TokenType... operators) {
        Expr left = expressionHandler.handle();
        while (match(operators)) {
            Token operator = previous();
            Expr right = expressionHandler.handle();
            left = new BinaryExpr(left, operator, right);
        }
        return left;
    }
}
