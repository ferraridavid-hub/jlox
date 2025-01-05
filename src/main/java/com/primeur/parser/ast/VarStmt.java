package com.primeur.parser.ast;

import com.primeur.lexer.Token;

public class VarStmt extends Stmt {

	private final Token name;
	private final Expr initializer;

	public VarStmt(Token name, Expr initializer) {
		this.name = name;
		this.initializer = initializer;
	}

	 public Token getName() {
		return this.name;
	}

	 public Expr getInitializer() {
		return this.initializer;
	}

	@Override
	public <R> R accept(StmtVisitor<R> visitor) {
		return visitor.visitVarStmt(this);
	}
}
