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
package org.cubeengine.linguist.translation;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests a {@link PluralTranslation}.
 */
public class PluralTranslationTest
{
    @Test
    public void testPluralWithoutContext() throws Exception
    {
        final String context = null;
        final String singularId = "service";
        final String pluralId = "services";
        final String[] translationStrings = {"Service", "Services"};

        final Translation translation = new PluralTranslation(context, singularId, pluralId, translationStrings);

        assertFalse(translation.hasContext());
        assertEquals(context, translation.getContext());
        assertEquals(singularId, translation.getSingularId());
        assertTrue(translation.isPluralTranslation());
        assertEquals(pluralId, translation.getPluralId());
        assertEquals(translationStrings.length, translation.getTranslationCount());
        assertEquals(translationStrings[0], translation.getTranslation(0));
        assertEquals(translationStrings[1], translation.getTranslation(1));

        final String newTranslation = "new Service";
        final String newTranslation2 = "new Services";
        translation.setTranslation(newTranslation, 0);
        translation.setTranslation(newTranslation2, 1);

        assertEquals(newTranslation, translation.getTranslation(0));
        assertEquals(newTranslation2, translation.getTranslation(1));

        try
        {
            translation.setTranslation(newTranslation, 3);
            fail("Set translation index one worked. That's strange");
        }
        catch (final TranslationException ignored)
        {}

        try
        {
            translation.getTranslation(3);
            fail("Get translation with index one worked. That's strange");
        }
        catch (final TranslationException ignored)
        {}
    }

    @Test
    public void testPluralWithContext() throws Exception
    {
        final String context = "global";
        final String singularId = "service";
        final String pluralId = "services";
        final String[] translationStrings = {"Service", "Services"};

        final Translation translation = new PluralTranslation(context, singularId, pluralId, translationStrings);

        assertTrue(translation.hasContext());
        assertEquals(context, translation.getContext());
        assertEquals(singularId, translation.getSingularId());
        assertTrue(translation.isPluralTranslation());
        assertEquals(pluralId, translation.getPluralId());
        assertEquals(translationStrings.length, translation.getTranslationCount());
        assertEquals(translationStrings[0], translation.getTranslation(0));
        assertEquals(translationStrings[1], translation.getTranslation(1));

        final String newTranslation = "new Service";
        final String newTranslation2 = "new Services";
        translation.setTranslation(newTranslation, 0);
        translation.setTranslation(newTranslation2, 1);

        assertEquals(newTranslation, translation.getTranslation(0));
        assertEquals(newTranslation2, translation.getTranslation(1));

        try
        {
            translation.setTranslation(newTranslation, 3);
            fail("Set translation index one worked. That's strange");
        }
        catch (final TranslationException ignored)
        {}

        try
        {
            translation.getTranslation(3);
            fail("Get translation with index one worked. That's strange");
        }
        catch (final TranslationException ignored)
        {}
    }
}
