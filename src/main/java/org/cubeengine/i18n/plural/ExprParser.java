/*
 * The MIT License
 * Copyright © 2013 Cube Island
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.cubeengine.i18n.plural;

/**
 * expr         := disjunction
 * disjunction  := conjunction '||' expr
 *               | conjunction
 * conjunction  := comparison '&&' expr
 *               | comparison
 * comparison   := sum comparisonOp expr
 *               | sum
 * comparisonOp := '>=' | '<=' | '<' | '>' | '==' | '!='
 * sum          := product sumOp expr
 *               | product
 * sumOp        := '+' | '-'
 * product      := factor productOp expr
 *               | factor
 * productOp    := '*' | '/' | '%'
 * factor       := variable
 *               | literal
 *               | '(' expr ')'
 *               | '!' expr
 *
 * ??? ->
 * ternary      := expr '?' expr ':' expr
 */
public class ExprParser {
    private static final String EQUALS = "==";
    private static final String NOT_EQUALS = "!=";
    private static final String GREATER = ">";
    private static final String LESS = "<";
    private static final String LESS_OR_EQUALS = "<=";
    private static final String GREATER_OR_EQUALS = ">=";
    private static final String MULTIPLY = "*";
    private static final String DIVIDE = "/";
    private static final String MODULO = "%";
    private static final String PLUS = "+";
    private static final String MINUS = "-";
    private static final String AND = "&&";
    private static final String OR = "||";
    private static final String NOT = "!";
    private static final String OPEN_PARENTHESES = "(";
    private static final String CLOSE_PARENTHESES = ")";

    public static abstract class Expr {
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

    public static class LiteralExpr extends Expr {
        private final int literal;

        public LiteralExpr(int literal) {
            this.literal = literal;
        }

        public int eval(int n) {
            return literal;
        }

        @Override
        public String toString() {
            return "'" + literal + "'";
        }
    }

    public static class VariableExpr extends Expr {
        private static final VariableExpr VAR = new VariableExpr();

        public int eval(int n) {
            return n;
        }

        @Override
        public String toString() {
            return "n";
        }
    }

    public static class UnaryOperatorExpr extends Expr {
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
            return "(~" + value + ")";
        }
    }

    public static class BinaryOperatorExpr extends Expr {
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
            return "(" + lhs + "~" + rhs + ")";
        }
    }

    public static class TernaryOperatorExpr extends Expr {
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

    private static final class State {
        private final String s;
        private int offset;

        public State(String s) {
            this.s = s;
            this.offset = 0;
        }

        public char peek() {
            return this.s.charAt(offset);
        }

        public char pop() {
            return this.s.charAt(offset++);
        }

        public void consume() {
            this.offset++;
        }

        public boolean is(char c) {
            return peek() == c;
        }

        public boolean is(String s) {
            return s.indexOf(peek()) != -1;
        }

        public boolean hasMore() {
            return this.offset < this.s.length();
        }

        public boolean hasMore(int n) {
            return this.offset + n < this.s.length();
        }

        public char consume(char expected) {
            char c = pop();
            if (c != expected) {
                throw new RuntimeException("expected=" + expected + ", got=" + c);
            }
            return c;
        }

        public char consume(String expected) {
            char c = pop();
            if (expected.indexOf(c) == -1) {
                throw new RuntimeException("expected=" + expected + ", got=" + c);
            }
            return c;
        }

        @Override
        public String toString() {
            return "State(offset=" + offset + ", " + (hasMore() ? "current=" + peek() : "depleted") + ")";
        }
    }

    public static Expr parse(String exprString) {
        State s = new State(exprString.replaceAll("\\s+", ""));
        final Expr result = parseExpr(s);
        if (s.hasMore()) {
            throw new RuntimeException("Result is incomplete!");
        }
        return result;
    }

    private static Expr parseExpr(State s) {
        return parseDisjunction(s);
    }

    private static Expr parseDisjunction(State s) {
        Expr lhs = parseConjunction(s);
        if (s.hasMore(3) && s.is('|')) {
            s.consume();
            s.consume('|');
            BinaryOperator op = new BinaryOperator() {
                @Override
                public int apply(int lhs, int rhs) {
                    return Expr.toInt(Expr.toBool(lhs) || Expr.toBool(rhs));
                }
            };
            Expr rhs = parseExpr(s);

            return new BinaryOperatorExpr(lhs, op, rhs);
        } else {
            return lhs;
        }
    }

    private static Expr parseConjunction(State s) {
        Expr lhs = parseComparison(s);
        if (s.hasMore(3) && s.is('&')) {
            s.consume();
            s.consume('&');
            BinaryOperator op = new BinaryOperator() {
                @Override
                public int apply(int lhs, int rhs) {
                    return Expr.toInt(Expr.toBool(lhs) && Expr.toBool(rhs));
                }
            };
            Expr rhs = parseExpr(s);

            return new BinaryOperatorExpr(lhs, op, rhs);
        } else {
            return lhs;
        }
    }

    private static Expr parseComparison(State s) {
        Expr lhs = parseSum(s);
        if (s.hasMore() && s.is("<>!=")) {
            BinaryOperator op = parseComparisonOp(s);
            Expr rhs = parseExpr(s);

            return new BinaryOperatorExpr(lhs, op, rhs);
        } else {
            return lhs;
        }
    }

    private static Expr parseSum(State s) {
        Expr lhs = parseProduct(s);
        if (s.hasMore() && s.is("+-")) {
            BinaryOperator op = parseSumOp(s);
            Expr rhs = parseExpr(s);

            return new BinaryOperatorExpr(lhs, op, rhs);
        } else {
            return lhs;
        }
    }

    private static Expr parseProduct(State s) {
        Expr lhs = parseFactor(s);
        if (s.hasMore() && s.is("*/%")) {
            BinaryOperator op = parseProductOp(s);
            Expr rhs = parseExpr(s);

            return new BinaryOperatorExpr(lhs, op, rhs);
        } else {
            return lhs;
        }
    }

    private static Expr parseFactor(State s) {
        final char c = s.peek();
        if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) {
            return parseVariable(s);
        } else if (c == '(') {
            s.consume();
            Expr e = parseExpr(s);
            s.consume(')');
            return e;
        } else if (c == '!') {
            return parseNegationn(s);
        } else {
            return parseLiteral(s);
        }
    }

    private static Expr parseVariable(State s) {
        s.consume();
        return VariableExpr.VAR;
    }

    private static Expr parseLiteral(State s) {
        int signum = 1;
        switch (s.peek()) {
            case '+':
                s.consume();
                break;
            case '-':
                signum = -1;
                s.consume();
        }

        if (!Character.isDigit(s.peek())) {
            throw new RuntimeException("no number!");
        }

        int number = parseNumber(s, 0, true);

        return new LiteralExpr(signum * number);
    }

    private static int parseNumber(State s, int current, boolean first) {
        int digit = s.pop() - '0';
        if (digit == 0 && first) {
            return 0;
        }

        final int next = current * 10 + digit;
        if (s.hasMore() && Character.isDigit(s.peek())) {
            return parseNumber(s, next, false);
        } else {
            return next;
        }
    }

    private static Expr parseTernary(State s) {
        Expr condition = parseExpr(s);
        s.consume('?');
        Expr truePath = parseExpr(s);
        s.consume(':');
        Expr falsePath = parseExpr(s);

        return new TernaryOperatorExpr(condition, truePath, falsePath);
    }

    private static Expr parseNegationn(State s) {
        s.consume('!');
        UnaryOperator op = new UnaryOperator() {
            @Override
            public int apply(int v) {
                return v != 0 ? 0 : 1;
            }
        };
        Expr value = parseExpr(s);

        return new UnaryOperatorExpr(op, value);
    }

    private static BinaryOperator parseComparisonOp(State s) {
        final char first = s.consume("><!=");
        switch (first) {
            case '>':
                if (s.peek() == '=') {
                    s.consume();
                    return new ComparisonOperator() {
                        @Override
                        public boolean compare(int lhs, int rhs) {
                            return lhs >= rhs;
                        }
                    };
                } else {
                    return new ComparisonOperator() {
                        @Override
                        public boolean compare(int lhs, int rhs) {
                            return lhs > rhs;
                        }
                    };
                }
            case '<':
                if (s.peek() == '=') {
                    s.consume();
                    return new ComparisonOperator() {
                        @Override
                        public boolean compare(int lhs, int rhs) {
                            return lhs <= rhs;
                        }
                    };
                } else {
                    return new ComparisonOperator() {
                        @Override
                        public boolean compare(int lhs, int rhs) {
                            return lhs < rhs;
                        }
                    };
                }
            case '!':
                s.consume('=');
                return new ComparisonOperator() {
                    @Override
                    public boolean compare(int lhs, int rhs) {
                        return lhs != rhs;
                    }
                };
            default:
                s.consume('=');
                return new ComparisonOperator() {
                    @Override
                    public boolean compare(int lhs, int rhs) {
                        return lhs == rhs;
                    }
                };
        }
    }

    private static BinaryOperator parseProductOp(State s) {
        switch (s.consume("*/%")) {
            case '*':
                return new BinaryOperator() {
                    @Override
                    public int apply(int lhs, int rhs) {
                        return lhs * rhs;
                    }
                };
            case '/':
                return new BinaryOperator() {
                    @Override
                    public int apply(int lhs, int rhs) {
                        return lhs / rhs;
                    }
                };
            default:
                return new BinaryOperator() {
                    @Override
                    public int apply(int lhs, int rhs) {
                        return lhs % rhs;
                    }
                };
        }
    }

    private static BinaryOperator parseSumOp(State s) {
        switch (s.consume("+-")) {
            case '+':
                return new BinaryOperator() {
                    @Override
                    public int apply(int lhs, int rhs) {
                        return lhs + rhs;
                    }
                };
            default:
                return new BinaryOperator() {
                    @Override
                    public int apply(int lhs, int rhs) {
                        return lhs - rhs;
                    }
                };
        }
    }
}
