package org.cubeengine.i18n.plural.parser;

public class VariableExpr extends Expr {
    public static final VariableExpr VAR = new VariableExpr();

    public int eval(int n) {
        return n;
    }

    @Override
    public String toString() {
        return "n";
    }
}
