package com.primeur.parser.ast;

public class LiteralExpr extends Expr {

	private final Object value;

	public LiteralExpr(Object value) {
		this.value = value;
	}

	 public Object getValue() {
		return this.value;
	}

	@Override
	public <R> R accept(ExprVisitor<R> visitor) {
		return visitor.visitLiteralExpr(this);
	}
}
