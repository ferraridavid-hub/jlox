package com.primeur.parser;

public class Grouping extends Expr {
	private final Expr right;

	public Grouping(Expr right) {
		this.right = right;
	}

	@Override
	<R> R accept(Visitor<R> visitor) {
		return visitor.visitGroupingExpr(this);
	}
}
