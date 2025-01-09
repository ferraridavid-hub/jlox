package com.primeur.parser.ast;

import com.primeur.lexer.Token;

public class LogicalExpr extends Expr {

	private final Expr left;
	private final Token operator;
	private final Expr right;

	public LogicalExpr(Expr left, Token operator, Expr right) {
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
	public <R> R accept(ExprVisitor<R> visitor) {
		return visitor.visitLogicalExpr(this);
	}
}
