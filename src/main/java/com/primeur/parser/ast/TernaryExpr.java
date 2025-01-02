package com.primeur.parser.ast;

import com.primeur.lexer.Token;

public class TernaryExpr extends Expr {

	private final Expr left;
	private final Token leftOperator;
	private final Expr middle;
	private final Token rightOperator;
	private final Expr right;

	public TernaryExpr(Expr left, Token leftOperator, Expr middle, Token rightOperator, Expr right) {
		this.left = left;
		this.leftOperator = leftOperator;
		this.middle = middle;
		this.rightOperator = rightOperator;
		this.right = right;
	}

	 public Expr getLeft() {
		return this.left;
	}

	 public Token getLeftOperator() {
		return this.leftOperator;
	}

	 public Expr getMiddle() {
		return this.middle;
	}

	 public Token getRightOperator() {
		return this.rightOperator;
	}

	 public Expr getRight() {
		return this.right;
	}

	@Override
	public <R> R accept(Visitor<R> visitor) {
		return visitor.visitTernaryExpr(this);
	}
}
