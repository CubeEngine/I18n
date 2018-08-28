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
 * expr        := sum | '(' expr ')' | value
 * value       := variable | literal | ternary
 * sum         := product '+' product | product '-' product | product
 * product     := expr '*' expr | expr '/' expr | expr '%' expr | expr
 * ternary     := boolExpr '?' expr ':' expr
 * boolExpr    := disjunction | '(' boolExpr ')' | negation | comparison | value
 * disjunction := conjunction '||' conjunction | conjunction
 * conjunction := boolExpr '&&' boolExpr | boolExpr
 * negation    := '!' boolExpr
 * comparison  := boolExpr '>=' boolExpr | boolExpr '<=' boolExpr |
 *                boolExpr '>'  boolExpr | boolExpr '<'  boolExpr |
 *                boolExpr '==' boolExpr | boolExpr '!=' boolExpr
 */
public class ExprParser
{
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

    public interface Expr {
        int eval(int n);
        boolean isTrue(int n);
    }

    public static class LiteralExpr implements Expr {
        private final int literal;

        public LiteralExpr(int literal)
        {
            this.literal = literal;
        }

        public int eval(int n)
        {
            return literal;
        }

        public boolean isTrue(int n)
        {
            return literal != 0;
        }
    }

    public static class VariableExpr implements Expr {
        private static final VariableExpr VAR = new VariableExpr();

        public int eval(int n)
        {
            return n;
        }

        public boolean isTrue(int n)
        {
            return n != 0;
        }
    }

    public static Expr parse(String exprString) {
        return VariableExpr.VAR;
    }
}
