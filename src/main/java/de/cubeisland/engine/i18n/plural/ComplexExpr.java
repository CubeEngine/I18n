/**
 * The MIT License
 * Copyright (c) 2013 Cube Island
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
package de.cubeisland.engine.i18n.plural;

import org.codehaus.commons.compiler.CompileException;
import org.codehaus.janino.ExpressionEvaluator;

import java.lang.reflect.InvocationTargetException;

public class ComplexExpr implements PluralExpr
{
    private static final String[] ARG_NAMES = {"n"};
    private static final Class[] ARG_TYPES = {int.class};

    private final ExpressionEvaluator ee;

    public ComplexExpr(String expression)
    {
        try
        {
            this.ee = new ExpressionEvaluator(expression, int.class, ARG_NAMES, ARG_TYPES);
        }
        catch (CompileException e)
        {
            throw new IllegalArgumentException("Failed to compile the given expression '" + expression + "' !", e);
        }
    }

    public int evaluate(int n)
    {
        try
        {
            return (Integer)ee.evaluate(new Object[] {n});
        }
        catch (InvocationTargetException e)
        {
            throw new PluralExpressionEvaluationException("Failed to evaluate a plural expression!", e);
        }
    }
}
