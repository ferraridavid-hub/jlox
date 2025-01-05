package com.primeur.parser.ast;

public interface StmtVisitor<R> {
	R visitExpressionStmt(ExpressionStmt expressionStmt);
	R visitPrintStmt(PrintStmt printStmt);
	R visitVarStmt(VarStmt varStmt);
}
