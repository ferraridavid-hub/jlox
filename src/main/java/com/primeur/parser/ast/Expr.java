package com.primeur.parser.ast;

public abstract class Expr {
	public abstract <R> R accept(ExprVisitor<R> visitor);
}
