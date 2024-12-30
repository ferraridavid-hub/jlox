package com.primeur.parser;

import com.primeur.lexer.Token;

public class UnaryExpr extends Expr {

	private final Token operator;
	private final Expr right;

	public UnaryExpr(Token operator, Expr right) {
		this.operator = operator;
		this.right = right;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitUnaryExpr(this);
	}

	public Token getOperator() {
		return operator;
	}

	public Expr getRight() {
		return right;
	}
}
