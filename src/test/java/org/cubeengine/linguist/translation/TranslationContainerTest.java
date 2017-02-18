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

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests the {@link TranslationContainer}.
 */
public class TranslationContainerTest
{
    @Test
    public void testContainer() throws Exception
    {
        final TranslationContainer container = new TranslationContainer();

        container.addTranslation(new SingularTranslation(null, "service", "Service"));
        container.putTranslation("global", "service", null, "global Service", 0, 1);
        assertEquals("Service", container.getTranslation(null, "service", null, 0));
        assertEquals("global Service", container.getTranslation("global", "service", null, 0));

        container.addTranslation(new SingularTranslation(null, "service", "Service2"));
        assertEquals("Service2", container.getTranslation(null, "service", null, 0));

        container.putTranslation(null, "a man", "%d men", "ein Mann", 0, 2);
        container.putTranslation(null, "a man", "%d men", "%d Männer", 1, 2);
        assertEquals("ein Mann", container.getTranslation(null, "a man", "%d men", 0));
        assertEquals("%d Männer", container.getTranslation(null, "a man", "%d men", 1));

        try
        {
            container.getTranslation(null, "a man", null, 0);
            fail("Loading a plural message without plural index should fail");
        }
        catch (final TranslationException ignored)
        {}

        assertNull(container.getTranslation("be", "fsd", null, 0));
    }
}
