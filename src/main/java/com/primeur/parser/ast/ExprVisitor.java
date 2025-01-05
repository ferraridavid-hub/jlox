package com.primeur.parser.ast;

public interface ExprVisitor<R> {
	R visitBinaryExpr(BinaryExpr binaryExpr);
	R visitGroupingExpr(GroupingExpr groupingExpr);
	R visitLiteralExpr(LiteralExpr literalExpr);
	R visitUnaryExpr(UnaryExpr unaryExpr);
	R visitTernaryExpr(TernaryExpr ternaryExpr);
}
