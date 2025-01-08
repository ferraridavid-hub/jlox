package com.primeur.parser.ast;

import com.primeur.lexer.Token;

public class AssignExpr extends Expr {

	private final Token name;
	private final Expr value;

	public AssignExpr(Token name, Expr value) {
		this.name = name;
		this.value = value;
	}

	 public Token getName() {
		return this.name;
	}

	 public Expr getValue() {
		return this.value;
	}

	@Override
	public <R> R accept(ExprVisitor<R> visitor) {
		return visitor.visitAssignExpr(this);
	}
}
