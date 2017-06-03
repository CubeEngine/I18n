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
package org.cubeengine.linguist.plural;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the {@link ComplexExpr}.
 */
public class ComplexExprTest
{
    @Test(expected = IllegalArgumentException.class)
    public void testWrongExpression() throws Exception
    {
        new ComplexExpr("n==1;");
    }

    @Test
    public void testIrishEvaluate() throws Exception
    {
        // Irish expression; plural count 5
        final ComplexExpr expr = new ComplexExpr("n==1 ? 0 : n==2 ? 1 : (n>2 && n<7) ? 2 :(n>6 && n<11) ? 3 : 4");

        assertEquals(4, expr.evaluate(0));
        assertEquals(0, expr.evaluate(1));
        assertEquals(1, expr.evaluate(2));
        assertEquals(2, expr.evaluate(3));
        assertEquals(2, expr.evaluate(4));
        assertEquals(2, expr.evaluate(5));
        assertEquals(2, expr.evaluate(6));
        assertEquals(3, expr.evaluate(7));
        assertEquals(3, expr.evaluate(8));
        assertEquals(3, expr.evaluate(9));
        assertEquals(3, expr.evaluate(10));
        assertEquals(4, expr.evaluate(11));
        assertEquals(4, expr.evaluate(12));
        assertEquals(4, expr.evaluate(100));
        assertEquals(4, expr.evaluate(3232));
    }

    @Test
    public void testScottishGaelicEvaluate() throws Exception
    {
        // Scottish Gaelic expression; plural count 4
        final ComplexExpr expr = new ComplexExpr("(n==1 || n==11) ? 0 : (n==2 || n==12) ? 1 : (n > 2 && n < 20) ? 2 : 3");

        assertEquals(3, expr.evaluate(0));
        assertEquals(0, expr.evaluate(1));
        assertEquals(1, expr.evaluate(2));
        assertEquals(2, expr.evaluate(3));
        assertEquals(2, expr.evaluate(4));
        assertEquals(2, expr.evaluate(5));
        assertEquals(2, expr.evaluate(6));
        assertEquals(2, expr.evaluate(7));
        assertEquals(2, expr.evaluate(8));
        assertEquals(2, expr.evaluate(9));
        assertEquals(2, expr.evaluate(10));
        assertEquals(0, expr.evaluate(11));
        assertEquals(1, expr.evaluate(12));
        assertEquals(2, expr.evaluate(13));
        assertEquals(2, expr.evaluate(14));
        assertEquals(2, expr.evaluate(15));
        assertEquals(2, expr.evaluate(16));
        assertEquals(2, expr.evaluate(17));
        assertEquals(2, expr.evaluate(18));
        assertEquals(2, expr.evaluate(19));
        assertEquals(3, expr.evaluate(20));
        assertEquals(3, expr.evaluate(21));
        assertEquals(3, expr.evaluate(22));
        assertEquals(3, expr.evaluate(100));
        assertEquals(3, expr.evaluate(3232));

    }

    @Test
    public void testCroatianEvaluate() throws Exception
    {
        // Croatian expression; plural count 3
        final ComplexExpr expr = new ComplexExpr("n%10==1 && n%100!=11 ? 0 : n%10>=2 && n%10<=4 && (n%100<10 || n%100>=20) ? 1 : 2");

        assertEquals(0, expr.evaluate(1));

        assertEquals(1, expr.evaluate(2));
        assertEquals(1, expr.evaluate(3));
        assertEquals(1, expr.evaluate(4));

        assertEquals(2, expr.evaluate(5));
        assertEquals(2, expr.evaluate(6));
        assertEquals(2, expr.evaluate(7));
        assertEquals(2, expr.evaluate(8));
        assertEquals(2, expr.evaluate(9));
        assertEquals(2, expr.evaluate(10));
        assertEquals(2, expr.evaluate(11));
        assertEquals(2, expr.evaluate(12));

        assertEquals(0, expr.evaluate(21));
        assertEquals(0, expr.evaluate(31));

        assertEquals(2, expr.evaluate(100));
    }
}
