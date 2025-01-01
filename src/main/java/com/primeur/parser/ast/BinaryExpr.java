package com.primeur.parser.ast;

import com.primeur.lexer.Token;

public class BinaryExpr extends Expr {

	private final Expr left;
	private final Token operator;
	private final Expr right;

	public BinaryExpr(Expr left, Token operator, Expr right) {
		this.left = left;
		this.operator = operator;
		this.right = right;
	}

	 public Expr getLeft() {
		return this.left;
	}

	 public Token getOperator() {
		return this.operator;
	}

	 public Expr getRight() {
		return this.right;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitBinaryExpr(this);
	}
}
