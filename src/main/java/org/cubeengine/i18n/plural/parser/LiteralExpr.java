package org.cubeengine.i18n.plural.parser;

public class LiteralExpr extends Expr {
    private final int literal;

    public LiteralExpr(int literal) {
        this.literal = literal;
    }

    public int eval(int n) {
        return literal;
    }

    @Override
    public String toString() {
        return "(" + literal + ")";
    }
}
