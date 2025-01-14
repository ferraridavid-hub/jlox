package com.primeur.parser.ast;

import com.primeur.lexer.Token;

import java.util.List;

public class CallExpr extends Expr {

	private final Expr callee;
	private final Token paren;
	private final List<Expr> arguments;

	public CallExpr(Expr callee, Token paren, List<Expr> arguments) {
		this.callee = callee;
		this.paren = paren;
		this.arguments = arguments;
	}

	 public Expr getCallee() {
		return this.callee;
	}

	 public Token getParen() {
		return this.paren;
	}

	 public List<Expr> getArguments() {
		return this.arguments;
	}

	@Override
	public <R> R accept(ExprVisitor<R> visitor) {
		return visitor.visitCallExpr(this);
	}
}
