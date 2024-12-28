package com.primeur.parser;

import com.primeur.lexer.Token;

public class Unary extends Expr {
	private final Token operator;
	private final Expr right;

	public Unary(Token operator, Expr right) {
		this.operator = operator;
		this.right = right;
	}

	@Override
	<R> R accept(Visitor<R> visitor) {
		return visitor.visitUnaryExpr(this);
	}
}
