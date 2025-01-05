package com.primeur.parser.ast;

public class ExpressionStmt extends Stmt {

	private final Expr expression;

	public ExpressionStmt(Expr expression) {
		this.expression = expression;
	}

	 public Expr getExpression() {
		return this.expression;
	}

	@Override
	public <R> R accept(StmtVisitor<R> visitor) {
		return visitor.visitExpressionStmt(this);
	}
}
