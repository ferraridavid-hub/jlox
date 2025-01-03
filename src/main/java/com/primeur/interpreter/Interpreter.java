package com.primeur.interpreter;

import java.util.Objects;

import com.primeur.lexer.TokenType;
import com.primeur.parser.ast.BinaryExpr;
import com.primeur.parser.ast.Expr;
import com.primeur.parser.ast.GroupingExpr;
import com.primeur.parser.ast.LiteralExpr;
import com.primeur.parser.ast.TernaryExpr;
import com.primeur.parser.ast.UnaryExpr;
import com.primeur.parser.ast.Visitor;

public class Interpreter implements Visitor<Object> {

    public Object eval(Expr expr) {
        return expr.accept(this);
    }

    @Override
    public Object visitBinaryExpr(BinaryExpr binaryExpr) {
        Object leftValue = evaluate(binaryExpr.getLeft());
        Object rightValue = evaluate(binaryExpr.getRight());
        return switch (binaryExpr.getOperator().getType()) {
            case MINUS -> (double) leftValue - (double) rightValue;
            case STAR -> (double) leftValue * (double) rightValue;
            case SLASH -> (double) leftValue / (double) rightValue;
            case PLUS -> {
                if (leftValue instanceof Double dLeft && rightValue instanceof Double dRight) {
                    yield dLeft + dRight;
                }
                if (leftValue instanceof String sLeft && rightValue instanceof String sRight) {
                    yield sLeft + sRight;
                }
                yield null;
            }
            case GREATER -> (double) leftValue > (double) rightValue;
            case GREATER_EQUAL -> (double) leftValue >= (double) rightValue;
            case LESS -> (double) leftValue < (double) rightValue;
            case LESS_EQUAL -> (double) leftValue <= (double) rightValue;
            case EQUAL_EQUAL -> isEqual(leftValue, rightValue);
            case BANG_EQUAL -> ! isEqual(leftValue, rightValue);
            default -> null;
        };
    }

    @Override
    public Object visitGroupingExpr(GroupingExpr groupingExpr) {
        return evaluate(groupingExpr.getExpression());
    }

    @Override
    public Object visitLiteralExpr(LiteralExpr literalExpr) {
        return literalExpr.getValue();
    }

    @Override
    public Object visitUnaryExpr(UnaryExpr unaryExpr) {
        Object value = evaluate(unaryExpr.getRight());
        return switch (unaryExpr.getOperator().getType()) {
            case MINUS -> -(Double) value;
            case BANG -> !isTruthy(value);
            default -> null;
        };
    }

    @Override
    public Object visitTernaryExpr(TernaryExpr ternaryExpr) {
        throw new UnsupportedOperationException("Unimplemented method 'visitTernaryExpr'");
    }

    private Object evaluate(Expr expr) {
        return expr.accept(this);
    }

    private boolean isEqual(Object leftValue, Object rightValue) {
        return Objects.equals(leftValue, rightValue);
    }

    private boolean isTruthy(Object value) {
        if (value == null) {
            return false;
        }
        if (value instanceof Boolean boolValue) {
            return boolValue;
        }
        return true;
    }

}
