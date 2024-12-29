package com.primeur.parser;

public interface Visitor<R> {
	R visitBinaryExpr(BinaryExpr binaryExpr);
	R visitGroupingExpr(GroupingExpr groupingExpr);
	R visitLiteralExpr(LiteralExpr literalExpr);
	R visitUnaryExpr(UnaryExpr unaryExpr);
}
