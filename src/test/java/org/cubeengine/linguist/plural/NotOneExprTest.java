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
 * Tests the {@link NotOneExpr}.
 */
public class NotOneExprTest
{
    private final NotOneExpr notOneExpr = new NotOneExpr();

    @Test
    public void evaluate() throws Exception
    {
        assertEquals(1, this.notOneExpr.evaluate(0));
        assertEquals(0, this.notOneExpr.evaluate(1));
        assertEquals(1, this.notOneExpr.evaluate(2));
        assertEquals(1, this.notOneExpr.evaluate(3));
        assertEquals(1, this.notOneExpr.evaluate(4));
        assertEquals(1, this.notOneExpr.evaluate(5));
        assertEquals(1, this.notOneExpr.evaluate(100));
        assertEquals(1, this.notOneExpr.evaluate(4432));
    }
}
