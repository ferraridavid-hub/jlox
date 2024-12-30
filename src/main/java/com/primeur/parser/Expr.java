package com.primeur.parser;

public abstract class Expr {
	public abstract <R> R accept(Visitor<R> visitor);
}
