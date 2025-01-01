package com.primeur.parser.ast;

public class GroupingExpr extends Expr {

	private final Expr expression;

	public GroupingExpr(Expr expression) {
		this.expression = expression;
	}

	 public Expr getExpression() {
		return this.expression;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitGroupingExpr(this);
	}
}
