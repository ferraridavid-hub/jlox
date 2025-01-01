package com.primeur.parser.debug;

import com.primeur.lexer.Token;
import com.primeur.lexer.TokenType;
import com.primeur.parser.ast.*;

public class AstPrinter implements Visitor<String> {

    public String print(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public String visitBinaryExpr(BinaryExpr binaryExpr) {
        return parenthesize(binaryExpr.getOperator().getLexeme(), binaryExpr.getLeft(), binaryExpr.getRight());
    }

    @Override
    public String visitGroupingExpr(GroupingExpr groupingExpr) {
        return parenthesize("group", groupingExpr.getExpression());
    }

    @Override
    public String visitLiteralExpr(LiteralExpr literalExpr) {
        if (literalExpr.getValue() == null) {
            return "nil";
        }
        return literalExpr.getValue().toString();
    }

    @Override
    public String visitUnaryExpr(UnaryExpr unaryExpr) {
        return parenthesize(unaryExpr.getOperator().getLexeme(), unaryExpr.getRight());
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder builder = new StringBuilder();
        builder.append("(").append(name);
        for (Expr expr : exprs) {
            builder.append(" ");
            builder.append(expr.accept(this));
        }
        builder.append(")");
        return builder.toString();
    }
}
