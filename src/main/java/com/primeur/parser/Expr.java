package com.primeur.parser;

public abstract class Expr {

    abstract <R> R accept(Visitor<R> visitor);
}
