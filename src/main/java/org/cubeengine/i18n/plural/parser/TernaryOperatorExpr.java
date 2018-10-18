package org.cubeengine.i18n.plural.parser;

public class TernaryOperatorExpr extends Expr {
    private final Expr condition;
    private final Expr truePath;
    private final Expr falsePath;

    public TernaryOperatorExpr(Expr condition, Expr truePath, Expr falsePath) {
        this.condition = condition;
        this.truePath = truePath;
        this.falsePath = falsePath;
    }

    @Override
    public int eval(int n) {
        return condition.isTrue(n) ? truePath.eval(n) : falsePath.eval(n);
    }

    @Override
    public String toString() {
        return "(" + condition + "?" + truePath + ":" + falsePath + ")";
    }
}
