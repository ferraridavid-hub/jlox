package com.primeur.parser.ast.utils;

import com.primeur.parser.ast.Expr;

@FunctionalInterface
public interface ExpressionHandler {
    Expr handle();
}