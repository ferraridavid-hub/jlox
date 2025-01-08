package com.primeur.interpreter;

import java.util.*;

import com.primeur.lexer.Token;

public class Environment {
    private final Environment enclosing;
    private final Map<String, Object> values = new HashMap<>();
    private final Set<String> unintialized = new HashSet<>();

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
        values.put(name, null);
        unintialized.add(name);
    }

    public Object get(Token name) {
        if (values.containsKey(name.getLexeme())){
            if (unintialized.contains(name.getLexeme())) {
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
            unintialized.remove(name.getLexeme());
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
