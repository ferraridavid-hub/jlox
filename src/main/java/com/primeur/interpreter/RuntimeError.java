package com.primeur.interpreter;

import com.primeur.lexer.Token;

public class RuntimeError extends RuntimeException{
    final Token token;

    public RuntimeError (Token token , String message) {
        super(message);
        this.token = token;
    }
}
