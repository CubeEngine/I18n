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

import org.cubeengine.i18n.plural.parser.*;

/*
 * expr         := disjunction
 * ternary      := conjunction ? ternary : ternary
 *               | conjunction
 * disjunction  := conjunction '||' disjunction
 *               | conjunction
 * conjunction  := comparison '&&' conjunction
 *               | comparison
 * comparison   := sum comparisonOp comparison
 *               | sum
 * comparisonOp := '>=' | '<=' | '<' | '>' | '==' | '!='
 * sum          := product sumOp sum
 *               | product
 * sumOp        := '+' | '-'
 * product      := factor productOp product
 *               | factor
 * productOp    := '*' | '/' | '%'
 * factor       := variable
 *               | literal
 *               | '(' expr ')'
 *               | '!' expr
 */
public class ExprParser {

    public static Expr parse(String exprString) {
        State s = new State(exprString.replaceAll("\\s+", ""));
        final Expr result = parseExpr(s);
        if (s.hasMore()) {
            throw new RuntimeException("Result is incomplete!");
        }
        return result;
    }

    private static Expr parseExpr(State s) {
        return parseTernary(s);
    }

    private static Expr parseTernary(State s) {
        Expr condition = parseDisjunction(s);
        if (s.hasMore() && s.peek() == '?') {
            s.consume();
            Expr truePath = parseTernary(s);
            s.consume(':');
            Expr falsePath = parseTernary(s);

            return new TernaryOperatorExpr(condition, truePath, falsePath);
        } else {
            return condition;
        }
    }

    private static Expr parseDisjunction(State s) {
        Expr lhs = parseConjunction(s);
        if (s.hasMore(3) && s.is('|')) {
            s.consume();
            s.consume('|');
            BinaryOperator op = namedBinary("||", new BinaryOperator() {
                @Override
                public int apply(int lhs, int rhs) {
                    return Expr.toInt(Expr.toBool(lhs) || Expr.toBool(rhs));
                }
            });
            Expr rhs = parseDisjunction(s);

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
            BinaryOperator op = namedBinary("&&", new BinaryOperator() {
                @Override
                public int apply(int lhs, int rhs) {
                    return Expr.toInt(Expr.toBool(lhs) && Expr.toBool(rhs));
                }
            });
            Expr rhs = parseConjunction(s);

            return new BinaryOperatorExpr(lhs, op, rhs);
        } else {
            return lhs;
        }
    }

    private static Expr parseComparison(State s) {
        Expr lhs = parseSum(s);
        if (s.hasMore() && s.is("<>!=")) {
            BinaryOperator op = parseComparisonOp(s);
            Expr rhs = parseComparison(s);

            return new BinaryOperatorExpr(lhs, op, rhs);
        } else {
            return lhs;
        }
    }

    private static Expr parseSum(State s) {
        Expr lhs = parseProduct(s);
        if (s.hasMore() && s.is("+-")) {
            BinaryOperator op = parseSumOp(s);
            Expr rhs = parseSum(s);

            return new BinaryOperatorExpr(lhs, op, rhs);
        } else {
            return lhs;
        }
    }

    private static Expr parseProduct(State s) {
        Expr lhs = parseFactor(s);
        if (s.hasMore() && s.is("*/%")) {
            BinaryOperator op = parseProductOp(s);
            Expr rhs = parseProduct(s);

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
            return parseNegation(s);
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

    private static Expr parseNegation(State s) {
        s.consume('!');
        UnaryOperator op = namedUnary("!%s", new UnaryOperator() {
            @Override
            public int apply(int v) {
                return v != 0 ? 0 : 1;
            }
        });
        Expr value = parseExpr(s);

        return new UnaryOperatorExpr(op, value);
    }

    private static BinaryOperator parseComparisonOp(State s) {
        final char first = s.consume("><!=");
        switch (first) {
            case '>':
                if (s.peek() == '=') {
                    s.consume();
                    return namedBinary(">=", new ComparisonOperator() {
                        @Override
                        public boolean compare(int lhs, int rhs) {
                            return lhs >= rhs;
                        }
                    });
                } else {
                    return namedBinary(">", new ComparisonOperator() {
                        @Override
                        public boolean compare(int lhs, int rhs) {
                            return lhs > rhs;
                        }
                    });
                }
            case '<':
                if (s.peek() == '=') {
                    s.consume();
                    return namedBinary("<=", new ComparisonOperator() {
                        @Override
                        public boolean compare(int lhs, int rhs) {
                            return lhs <= rhs;
                        }
                    });
                } else {
                    return namedBinary("<", new ComparisonOperator() {
                        @Override
                        public boolean compare(int lhs, int rhs) {
                            return lhs < rhs;
                        }
                    });
                }
            case '!':
                s.consume('=');
                return namedBinary("!=", new ComparisonOperator() {
                    @Override
                    public boolean compare(int lhs, int rhs) {
                        return lhs != rhs;
                    }
                });
            default:
                s.consume('=');
                return namedBinary("==", new ComparisonOperator() {
                    @Override
                    public boolean compare(int lhs, int rhs) {
                        return lhs == rhs;
                    }
                });
        }
    }

    private static BinaryOperator parseSumOp(State s) {
        switch (s.consume("+-")) {
            case '+':
                return namedBinary("+", new BinaryOperator() {
                    @Override
                    public int apply(int lhs, int rhs) {
                        return lhs + rhs;
                    }
                });
            default:
                return namedBinary("-", new BinaryOperator() {
                    @Override
                    public int apply(int lhs, int rhs) {
                        return lhs - rhs;
                    }
                });
        }
    }

    private static BinaryOperator parseProductOp(State s) {
        switch (s.consume("*/%")) {
            case '*':
                return namedBinary("*", new BinaryOperator() {
                    @Override
                    public int apply(int lhs, int rhs) {
                        return lhs * rhs;
                    }
                });
            case '/':
                return namedBinary("/", new BinaryOperator() {
                    @Override
                    public int apply(int lhs, int rhs) {
                        return lhs / rhs;
                    }
                });
            default:
                return namedBinary("%", new BinaryOperator() {
                    @Override
                    public int apply(int lhs, int rhs) {
                        return lhs % rhs;
                    }
                });
        }
    }

    private static UnaryOperator namedUnary(final String name, final UnaryOperator inner) {
        return new UnaryOperator() {
            @Override
            public int apply(int v) {
                return inner.apply(v);
            }

            @Override
            public String toString() {
                return name;
            }
        };
    }

    private static BinaryOperator namedBinary(final String name, final BinaryOperator inner) {
        return new BinaryOperator() {
            @Override
            public int apply(int lhs, int rhs) {
                return inner.apply(lhs, rhs);
            }

            @Override
            public String toString() {
                return name;
            }
        };
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
}
