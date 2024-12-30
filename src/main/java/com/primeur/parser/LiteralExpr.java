package com.primeur.parser;

public class LiteralExpr extends Expr {

	private final Object value;

	public LiteralExpr(Object value) {
		this.value = value;
	}

	 public Object getValue() {
		return this.value;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitLiteralExpr(this);
	}
}
