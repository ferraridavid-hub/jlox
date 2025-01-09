package com.primeur.interpreter;

import java.util.List;
import java.util.Objects;

import com.primeur.Lox;
import com.primeur.lexer.Token;
import com.primeur.lexer.TokenType;
import com.primeur.parser.ast.*;

public class Interpreter implements ExprVisitor<Object>, StmtVisitor<Void> {
    private Environment environment = new Environment();

    public void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch(RuntimeError e) {
            Lox.runtimeError(e);
        }
    }

    @Override
    public Object visitAssignExpr(AssignExpr assignExpr) {
        Object value = evaluate(assignExpr.getValue());
        environment.assign(assignExpr.getName(), value);
        return value;
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
                checkDivisionByZero(operator, rightValue);
                yield (double) leftValue / (double) rightValue;
            }
            case PLUS -> {
                if (leftValue instanceof Double dLeft && rightValue instanceof Double dRight) {
                    yield dLeft + dRight;
                }
                if (leftValue instanceof String sLeft && rightValue instanceof String sRight) {
                    yield sLeft + sRight;
                }
                if (leftValue instanceof String sLeft) {
                    yield sLeft + stringify(rightValue);
                }
                if (rightValue instanceof String sRight) {
                    yield stringify(leftValue) + sRight;
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
    public Object visitLogicalExpr(LogicalExpr logicalExpr) {
        Object leftValue = evaluate(logicalExpr.getLeft());
        if (logicalExpr.getOperator().getType().equals(TokenType.OR)) {
            if (isTruthy(leftValue)) {
                return leftValue;
            }
        } else {
            if (!isTruthy(leftValue)) {
                return leftValue;
            }
        }
        return evaluate(logicalExpr.getRight());
    }

    @Override
    public Object visitTernaryExpr(TernaryExpr ternaryExpr) {
        Expr leftExpression = ternaryExpr.getLeft();
        if (isTruthy(evaluate(leftExpression))) {
            return evaluate(ternaryExpr.getMiddle());
        } else {
            return evaluate(ternaryExpr.getRight());
        }
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

    private void checkDivisionByZero(Token operator, Object right) {
        double rightValue = (double) right;
        if (rightValue == 0) {
            throw new RuntimeError(operator, "Division by zero.");
        }
    }

    private String stringify(Object value) {
        switch (value) {
            case null -> {
                return "nil";
            }
            case Double v -> {
                String text = value.toString();
                if (text.endsWith(".0")) {
                    text = text.substring(0, text.length() - 2);
                }
                return text;
            }
            case String valueText -> {
                return "\"" + valueText + "\"";
            }
            default -> {
            }
        }

        return value.toString();
    }

    @Override
    public Void visitIfStmt(IfStmt ifStmt) {
        if(isTruthy(evaluate(ifStmt.getCondition()))) {
            execute(ifStmt.getThenBranch());
        } else if (ifStmt.getElseBranch() != null) {
            execute(ifStmt.getElseBranch());
        }
        return null;
    }

    @Override
    public Void visitBlockStmt(BlockStmt blockStmt) {
        executeBlock(blockStmt.getStatements(), new Environment(environment));
        return null;
    }

    private void executeBlock(List<Stmt> statements, Environment environment) {
        try {
            this.environment = environment;
            statements.forEach(this::execute);
        } finally {
            this.environment = this.environment.getEnclosing();
        }
    }

    @Override
    public Void visitExpressionStmt(ExpressionStmt expressionStmt) {
        evaluate(expressionStmt.getExpression());
        return null;
    }

    @Override
    public Void visitPrintStmt(PrintStmt printStmt) {
        Object value = evaluate(printStmt.getExpression());
        System.out.println(stringify(value));
        return null;
    }

    private void execute(Stmt statement) {
        statement.accept(this);
    }

    @Override
    public Void visitVarStmt(VarStmt varStmt) { // store the identifier value binding somewhere
        Object value = null;
        if (varStmt.getInitializer() != null) {
            value = evaluate(varStmt.getInitializer());
            environment.define(varStmt.getName().getLexeme(), value);
        } else {
            environment.define(varStmt.getName().getLexeme());
        }
        return null;
    }

    @Override
    public Void visitWhileStmt(WhileStmt whileStmt) {
        while(isTruthy(evaluate(whileStmt.getCondition()))) {
            execute(whileStmt.getBody());
        }
        return null;
    }

    @Override
    public Object visitVariableExpr(VariableExpr variableExpr) { // retrieve the value bound to the variable name
        return environment.get(variableExpr.getName());
    }

}
