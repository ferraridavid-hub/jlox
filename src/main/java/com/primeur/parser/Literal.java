package com.primeur.parser;

public class Literal extends Expr {
	private final Object value;

	public Literal(Object value) {
		this.value = value;
	}

	@Override
	<R> R accept(Visitor<R> visitor) {
		return visitor.visitLiteralExpr(this);
	}
}
