package org.cubeengine.i18n.plural.parser;

public abstract class Expr {
    public abstract int eval(int n);

    public boolean isTrue(int n) {
        return eval(n) != 0;
    }

    public static boolean toBool(int n) {
        return n != 0;
    }

    public static int toInt(boolean b) {
        return b ? 1 : 0;
    }

    @Override
    public String toString() {
        return "Expr(???)";
    }
}
