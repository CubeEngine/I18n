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

import org.junit.Test;

import static org.cubeengine.i18n.plural.ExprParser.parse;
import static org.junit.Assert.assertEquals;

public class ComplexExprTest
{
    @Test
    public void testLiterals() {
        assertEquals(100, parse("100").eval(0));
        assertEquals(123, parse("123").eval(0));
        assertEquals(0, parse("0").eval(0));
    }

    @Test
    public void testSimpleExpr() {
        assertEquals(0, parse("!1").eval(0));
        assertEquals(1, parse("!0").eval(0));
        assertEquals(2, parse("1 * 2").eval(0));
        assertEquals(4, parse("1 * 2 * 2").eval(0));
        assertEquals(5, parse("1 + 2 * 2").eval(0));
        assertEquals(6, parse("(1 + 2) * 2").eval(0));
        assertEquals(1, parse("(1 + 2 * 2) < 6").eval(0));
        assertEquals(1, parse("1 + 2 * 2 < 6").eval(0));
    }

    @Test
    public void testComplexExpr()
    {
        // some plural forms from http://docs.translatehouse.org/projects/localization-guide/en/latest/l10n/pluralforms.html
        this.testExpression("0", 0, 0, 0);
        this.testExpression("n > 1", 0, 0, 1, 1);
        this.testExpression("n != 1", 1, 0, 1, 1);
        this.testExpression("n%10==1 && n%100!=11 ? 0 : n != 0 ? 1 : 2", 2, 0, 1, 1);
        this.testExpression("n==1 ? 0 : n==2 ? 1 : 2", 2, 0, 1, 2);
        // TODO maybe do more?
    }

    private void testExpression(String expr, int... outputs)
    {
        System.out.println("Expression: " + expr);
        ComplexExpr expression = new ComplexExpr(expr);
        System.out.println("Parsed:     " + expression);
        for (int input = 0; input < outputs.length; input++)
        {
            System.out.println("\tInput:    " + input);
            final int expected = outputs[input];
            final int actual = expression.evaluate(input);
            System.out.println("\tExpected: " + expected);
            System.out.println("\tActual:   " + actual);
            assertEquals(actual, expected);
        }
    }
}
