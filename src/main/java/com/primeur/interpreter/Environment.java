package com.primeur.interpreter;

import java.util.*;

import com.primeur.lexer.Token;

public class Environment {
    private static final Object UNINITIALIZED = new Object();
    private final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();

    public Environment() {
        this.enclosing = null;
    }

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public void define(String name) {
        values.put(name, UNINITIALIZED);
    }

    public Object get(Token name) {
        if (values.containsKey(name.getLexeme())){
            Object value = values.get(name.getLexeme());
            if (value.equals(UNINITIALIZED)) {
                throw new RuntimeError(name, "Uninitialized variable '" + name.getLexeme() + "'.");
            }
            return values.get(name.getLexeme());
        }
        if (enclosing != null) {
            return enclosing.get(name);
        }
        throw new RuntimeError(name, "Undefined variable '" + name.getLexeme() + "'.");
    }

    public void assign(Token name, Object value) {
        if (values.containsKey(name.getLexeme())) {
            values.put(name.getLexeme(), value);
            return;
        }
        if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }
        throw new RuntimeError(name, "Undefined variable '" + name.getLexeme() + "'");
    }

    public Environment getEnclosing() {
        return enclosing;
    }

}
