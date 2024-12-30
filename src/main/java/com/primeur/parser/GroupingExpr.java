package com.primeur.parser;

public class GroupingExpr extends Expr {

	private final Expr expression;

	public GroupingExpr(Expr expression) {
		this.expression = expression;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitGroupingExpr(this);
	}

	public Expr getExpression() {
		return expression;
	}
}
