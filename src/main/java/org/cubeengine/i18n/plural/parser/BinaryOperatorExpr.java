package org.cubeengine.i18n.plural.parser;

public class BinaryOperatorExpr extends Expr {
    private final Expr lhs;
    private final BinaryOperator op;
    private final Expr rhs;

    public BinaryOperatorExpr(Expr lhs, BinaryOperator op, Expr rhs) {
        this.lhs = lhs;
        this.op = op;
        this.rhs = rhs;
    }

    @Override
    public int eval(int n) {
        return op.apply(lhs.eval(n), rhs.eval(n));
    }

    @Override
    public String toString() {
        return "(" + lhs + op + rhs + ")";
    }
}
