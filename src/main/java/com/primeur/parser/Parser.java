package com.primeur.parser;

import com.primeur.Lox;
import com.primeur.lexer.Token;
import com.primeur.lexer.TokenType;
import com.primeur.parser.ast.*;
import com.primeur.parser.ast.error.ParseError;
import com.primeur.parser.ast.utils.ExpressionHandler;

import java.util.ArrayList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Stmt> parse() {
        List<Stmt> declarations = new ArrayList<>();
        while (!isAtEnd()) {
            declarations.add(declaration());
        }
        return declarations;
    }

    private Stmt declaration() {
        try {
            if (match(TokenType.VAR)) {
                return varDeclaration();
            }
            return statement();
        } catch (ParseError e) {
            synchronize();
            return null;
        }
    }

    private Stmt varDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expected variable name.");
        Expr initializer = null;
        if (match(TokenType.EQUAL)) {
            initializer = expression();
        }
        consume(TokenType.SEMICOLON, "Expected ';' after variable declaration");
        return new VarStmt(name, initializer);
    }

    private Stmt statement() {
        if (match(TokenType.PRINT)) {
            return printStatement();
        }
        if (match(TokenType.IF)) {
            return ifStatement();
        }
//        if (match(TokenType.WHILE)) {
//            return whileStatement();
//        }
        if (match(TokenType.LEFT_BRACE)) {
            return new BlockStmt(block());
        }
        return expressionStatement();
    }

//    private Stmt whileStatement(){
//        consume(TokenType.LEFT_PAREN, "Expected '(' after 'while'.");
//        Expr condition = expression();
//        consume(TokenType.RIGHT_PAREN, "Expected ')' after condition.");
//        Stmt body = statement();
//        return new WhileStmt(condition, body);
//
//    }

    private Stmt ifStatement() {
        consume(TokenType.LEFT_PAREN, "Expected '(' after 'if'");
        Expr condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expected ')' after condition");
        Stmt ifBranch = statement();
        Stmt thenBranch = null;
        if (match(TokenType.ELSE)) {
            thenBranch = statement();
        }
        return new IfStmt(condition, ifBranch, thenBranch);

    }

    private List<Stmt> block(){
        List<Stmt> statements = new ArrayList<>();
        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }
        consume(TokenType.RIGHT_BRACE, "Expected '}' after block.");
        return statements;
    }

    private Stmt printStatement() {
        Expr expression = expression();
        consume(TokenType.SEMICOLON, "Expected ';' after value.");
        return new PrintStmt(expression);
    }

    protected Stmt expressionStatement() {
        Expr expression = expression();
        consume(TokenType.SEMICOLON, "Expected ';' after expression");
        return new ExpressionStmt(expression);
    }

    protected Expr expression() {
        return sequence();
    }

    private Expr sequence() {
        return leftAssociativeBinaryExpr(this::assignment, TokenType.COMMA);
    }

    private Expr assignment() {
        Expr expr = conditional();
        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            Expr value = assignment();  // right-recursive
            if (expr instanceof VariableExpr variableExpr) {
                Token name = variableExpr.getName();
                return new AssignExpr(name, value);
            }
            error(equals, "Invalid assignment target.");
        }
        return expr;
    }

    private Expr conditional() {
        Expr left = logicOr();
        if (match(TokenType.QUESTION)) {
            Token leftOperand = previous();
            Expr middle = conditional();
            Token rightOperand = consume(TokenType.COLON, "Expected : in ternary conditional operator.");
            Expr right = conditional();
            left = new TernaryExpr(left, leftOperand, middle, rightOperand, right);
        }
        return left;
    }

    private Expr logicOr(){
        Expr left = logicAnd();
        while (match(TokenType.OR)) {
            Token operator = previous();
            Expr right = logicAnd();
            left = new LogicalExpr(left, operator, right);
        }
        return left;
    }

    private Expr logicAnd(){
        Expr left = equality();
        while (match(TokenType.AND)) {
            Token operator = previous();
            Expr right = equality();
            left = new LogicalExpr(left, operator, right);
        }
        return left;
    }

    private Expr equality() {
        return leftAssociativeBinaryExpr(this::comparison, TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL);
    }

    private Expr comparison() {
        return leftAssociativeBinaryExpr(this::term, TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS,
                TokenType.LESS_EQUAL);
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
        } else if (match(TokenType.PLUS)) {
            Token operator = previous();
            error(operator, "Lox doesn't support unary + operator.");
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

        if (match(TokenType.IDENTIFIER)) {
            return new VariableExpr(previous());
        }

        if (match(TokenType.LEFT_PAREN)) {
            Expr expr = expression();
            consume(TokenType.RIGHT_PAREN, "Expected ')' after expression.");
            return new GroupingExpr(expr);
        }

        throw error(peek(), "Expected expression.");
    }

    boolean match(TokenType... types) {
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

    protected boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    protected Token consume(TokenType type, String message) {
        if (check(type)) {
            return advance();
        }
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Lox.error(token, "Parser", message);
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
        // error productions
        if (match(operators)) {
            Token operator = previous();
            leftAssociativeBinaryExpr(expressionHandler, operators); // discard right operand
            throw error(operator, "Missing left operand for operator " + operator.getLexeme() + ".");
        }
        Expr left = expressionHandler.handle();
        while (match(operators)) {
            Token operator = previous();
            Expr right = expressionHandler.handle();
            left = new BinaryExpr(left, operator, right);
        }
        return left;
    }
}
