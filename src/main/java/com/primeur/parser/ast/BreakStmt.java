package com.primeur.parser.ast;

public class BreakStmt extends Stmt {

	@Override
	public <R> R accept(StmtVisitor<R> visitor) {
		return visitor.visitBreakStmt(this);
	}
}
