package org.cubeengine.i18n.plural.parser;

public class UnaryOperatorExpr extends Expr {
    private final UnaryOperator op;
    private final Expr value;

    public UnaryOperatorExpr(UnaryOperator op, Expr value) {
        this.op = op;
        this.value = value;
    }

    @Override
    public int eval(int n) {
        return op.apply(value.eval(n));
    }

    @Override
    public String toString() {
        return "(" + String.format(op.toString(), value) + ")";
    }
}
