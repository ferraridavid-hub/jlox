package com.primeur.interpreter;

import java.util.Objects;

import com.primeur.lexer.Token;
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
        Token operator = binaryExpr.getOperator();
        Object rightValue = evaluate(binaryExpr.getRight());
        return switch (operator.getType()) {
            case MINUS -> {
                checkNumberOperands(operator, leftValue, rightValue);
                yield (double) leftValue - (double) rightValue;
            }
            case STAR -> {
                checkNumberOperands(operator, leftValue, rightValue);
                yield (double) leftValue * (double) rightValue;
            }
            case SLASH -> {
                checkNumberOperands(operator, leftValue, rightValue);
                yield (double) leftValue / (double) rightValue;
            }
            case PLUS -> {
                if (leftValue instanceof Double dLeft && rightValue instanceof Double dRight) {
                    yield dLeft + dRight;
                }
                if (leftValue instanceof String sLeft && rightValue instanceof String sRight) {
                    yield sLeft + sRight;
                }
                throw new RuntimeError(operator, "Operands must be two numbers or two strings.");
            }
            case GREATER -> {
                checkNumberOperands(operator, leftValue, rightValue);
                yield (double) leftValue > (double) rightValue;
            }
            case GREATER_EQUAL -> {
                checkNumberOperands(operator, leftValue, rightValue);
                yield (double) leftValue >= (double) rightValue;
            }
            case LESS -> {
                checkNumberOperands(operator, leftValue, rightValue);
                yield (double) leftValue < (double) rightValue;
            }
            case LESS_EQUAL -> {
                checkNumberOperands(operator, leftValue, rightValue);
                yield (double) leftValue <= (double) rightValue;
            }
            case EQUAL_EQUAL -> isEqual(leftValue, rightValue);
            case BANG_EQUAL -> !isEqual(leftValue, rightValue);
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
        Token operator = unaryExpr.getOperator();
        Object value = evaluate(unaryExpr.getRight());
        return switch (operator.getType()) {
            case MINUS -> {
                checkNumberOperand(operator, value);
                yield -(Double) value;
            }
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

    private void checkNumberOperand(Token operator, Object operand) {
        if (!(operand instanceof Double)) {
            throw new RuntimeError(operator, "Operand must be a number.");
        }
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (!(left instanceof Double && right instanceof Double)) {
            throw new RuntimeError(operator, "Operands must be numbers.");
        }
    }
}
