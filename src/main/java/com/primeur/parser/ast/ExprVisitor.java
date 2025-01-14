package com.primeur.parser.ast;

public interface ExprVisitor<R> {
	R visitAssignExpr(AssignExpr assignExpr);
	R visitBinaryExpr(BinaryExpr binaryExpr);
	R visitCallExpr(CallExpr callExpr);
	R visitGroupingExpr(GroupingExpr groupingExpr);
	R visitLiteralExpr(LiteralExpr literalExpr);
	R visitUnaryExpr(UnaryExpr unaryExpr);
	R visitLogicalExpr(LogicalExpr logicalExpr);
	R visitTernaryExpr(TernaryExpr ternaryExpr);
	R visitVariableExpr(VariableExpr variableExpr);
}
