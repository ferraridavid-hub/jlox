package com.primeur.parser.ast;

import com.primeur.lexer.Token;

public class VariableExpr extends Expr {

	private final Token name;

	public VariableExpr(Token name) {
		this.name = name;
	}

	 public Token getName() {
		return this.name;
	}

	@Override
	public <R> R accept(ExprVisitor<R> visitor) {
		return visitor.visitVariableExpr(this);
	}
}
