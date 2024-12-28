package com.primeur.parser;

import com.primeur.lexer.Token;

public class Binary extends Expr {
	private final Expr left;
	private final Token operator;
	private final Expr right;

	public Binary(Expr left, Token operator, Expr right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	@Override
	<R> R accept(Visitor<R> visitor) {
		return visitor.visitBinaryExpr(this);
	}
}
