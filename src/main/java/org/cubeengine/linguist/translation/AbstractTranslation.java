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
package org.cubeengine.linguist.translation;

public abstract class AbstractTranslation implements Translation
{
    private final String context;
    private final String singularId;

    protected AbstractTranslation(final String context, final String singularId)
    {
        this.context = context;
        this.singularId = singularId;
    }

    public boolean hasContext()
    {
        return this.context != null;
    }

    public String getContext()
    {
        return this.context;
    }

    public String getSingularId()
    {
        return this.singularId;
    }

    @Override
    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        final AbstractTranslation that = (AbstractTranslation)o;

        if (this.hasContext() ? !this.getContext().equals(that.getContext()) : that.getContext() != null)
        {
            return false;
        }
        if (!this.getSingularId().equals(that.getSingularId()))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = this.hasContext() ? this.getContext().hashCode() : 0;
        result = 31 * result + this.getSingularId().hashCode();
        return result;
    }
}
