package com.primeur.parser.ast;

public class IfStmt extends Stmt {

	private final Expr condition;
	private final Stmt thenBranch;
	private final Stmt elseBranch;

	public IfStmt(Expr condition, Stmt thenBranch, Stmt elseBranch) {
		this.condition = condition;
		this.thenBranch = thenBranch;
		this.elseBranch = elseBranch;
	}

	 public Expr getCondition() {
		return this.condition;
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
