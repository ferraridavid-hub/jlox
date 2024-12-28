package com.primeur.parser;

import com.primeur.lexer.Token;

public class Unary {
	private final Token operator;
	private final Expr right;

	public Unary(Token operator, Expr right) {
		this.operator = operator;
		this.right = right;
	}
}
