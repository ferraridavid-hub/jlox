package com.primeur.parser.debug;

import com.primeur.lexer.Token;
import com.primeur.lexer.TokenType;
import com.primeur.parser.*;

public class AstPrinter implements Visitor<String> {

    String print(Expr expr) {
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

    public static void main(String[] args) {
        Expr expression = new BinaryExpr(
                new UnaryExpr(
                        new Token(TokenType.MINUS, "-", null, 1),
                        new LiteralExpr(234)),
                new Token(TokenType.STAR, "*", null, 1),
                new GroupingExpr(
                        new LiteralExpr(45.65)
                )
        );
        System.out.println(new AstPrinter().print(expression));
    }
}
