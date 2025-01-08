package com.primeur.parser;

import com.primeur.lexer.Token;
import com.primeur.lexer.TokenType;
import com.primeur.parser.ast.Expr;
import com.primeur.parser.ast.ExpressionStmt;
import com.primeur.parser.ast.PrintStmt;
import com.primeur.parser.ast.Stmt;

import java.util.List;

public class ReplParser extends Parser{

    public ReplParser(List<Token> tokens) {
        super(tokens);
    }

    @Override
    protected Stmt expressionStatement() {
        Expr expression = expression();
        if (isAtEnd()) {
            return new PrintStmt(expression);
        }
        consume(TokenType.SEMICOLON, "Expected ';' after expression");
        return new ExpressionStmt(expression);
    }
}
