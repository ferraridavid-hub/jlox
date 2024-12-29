package com.primeur.parser;

public class GroupingExpr extends Expr {

	private final Expr right;

	public GroupingExpr(Expr right) {
		this.right = right;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitGroupingExpr(this);
	}
}
