package com.primeur.parser.ast;

public class PrintStmt extends Stmt {

	private final Expr expression;

	public PrintStmt(Expr expression) {
		this.expression = expression;
	}

	 public Expr getExpression() {
		return this.expression;
	}

	@Override
	public <R> R accept(StmtVisitor<R> visitor) {
		return visitor.visitPrintStmt(this);
	}
}
