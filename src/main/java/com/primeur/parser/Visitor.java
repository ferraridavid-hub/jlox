package com.primeur.parser;

public interface Visitor<R> {
	 R visitBinaryExpr(Binary binaryExpr);
	 R visitGroupingExpr(Grouping groupingExpr);
	 R visitLiteralExpr(Literal literalExpr);
	 R visitUnaryExpr(Unary unaryExpr);
}
