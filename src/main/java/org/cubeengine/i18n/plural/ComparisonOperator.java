package org.cubeengine.i18n.plural;

abstract class ComparisonOperator implements BinaryOperator {
    @Override
    public int apply(int lhs, int rhs) {
        return compare(lhs, rhs) ? 1 : 0;
    }

    public abstract boolean compare(int lhs, int rhs);
}
