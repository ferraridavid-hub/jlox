package com.primeur.parser.ast;

public class IfStmt extends Stmt {

	private final Expr expression;
	private final Stmt thenBranch;
	private final Stmt elseBranch;

	public IfStmt(Expr expression, Stmt thenBranch, Stmt elseBranch) {
		this.expression = expression;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	 public Expr getExpression() {
		return this.expression;
	}

	 public Stmt getThenBranch() {
		return this.thenBranch;
	}

	 public Stmt getElseBranch() {
		return this.elseBranch;
	}

	@Override
	public <R> R accept(StmtVisitor<R> visitor) {
		return visitor.visitIfStmt(this);
	}
}
