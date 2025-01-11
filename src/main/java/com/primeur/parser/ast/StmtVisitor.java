package com.primeur.parser.ast;

public interface StmtVisitor<R> {
	R visitBreakStmt(BreakStmt breakStmt);
	R visitIfStmt(IfStmt ifStmt);
	R visitBlockStmt(BlockStmt blockStmt);
	R visitExpressionStmt(ExpressionStmt expressionStmt);
	R visitPrintStmt(PrintStmt printStmt);
	R visitVarStmt(VarStmt varStmt);
	R visitWhileStmt(WhileStmt whileStmt);
}
