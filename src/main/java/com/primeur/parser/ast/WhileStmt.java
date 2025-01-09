package com.primeur.parser.ast;

public class WhileStmt extends Stmt {

	private final Expr condition;
	private final Stmt body;

	public WhileStmt(Expr condition, Stmt body) {
		this.condition = condition;
		this.body = body;
	}

	 public Expr getCondition() {
		return this.condition;
	}

	 public Stmt getBody() {
		return this.body;
	}

	@Override
	public <R> R accept(StmtVisitor<R> visitor) {
		return visitor.visitWhileStmt(this);
	}
}
