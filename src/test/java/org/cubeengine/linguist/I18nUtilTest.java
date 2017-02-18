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
package org.cubeengine.linguist;

import java.util.Locale;
import org.junit.Test;

import static org.cubeengine.linguist.I18nUtil.*;
import static org.junit.Assert.*;

/**
 * Tests the {@link I18nUtil} class.
 */
public class I18nUtilTest
{
    @Test
    public void testIsNullOrEmpty() throws Exception
    {
        assertTrue(isNullOrEmpty(""));
        assertTrue(isNullOrEmpty(null));
        assertFalse(isNullOrEmpty("h"));
    }

    @Test
    public void testIsNumeric() throws Exception
    {
        assertTrue(isNumeric("21"));
        assertTrue(isNumeric("0"));

        assertFalse(isNumeric(""));
        assertFalse(isNumeric("-32")); // actually this is numeric
        assertFalse(isNumeric("32d")); // this is numeric too, but the method doesn't treat them as numeric
        assertFalse(isNumeric("d93r"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNumericFail() throws Exception
    {
        isNumeric(null);
    }

    @Test
    public void testLocaleToLanguageTag() throws Exception
    {
        assertEquals("de-DE", localeToLanguageTag(Locale.GERMANY));
        assertEquals("de", localeToLanguageTag(Locale.GERMAN));
        assertEquals("en-US", localeToLanguageTag(Locale.US));
        assertEquals("en", localeToLanguageTag(Locale.ENGLISH));
        assertEquals("und", localeToLanguageTag(new Locale("")));
        assertEquals("und-DE", localeToLanguageTag(new Locale("", "DE")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void localeToLanguageTagFail() throws Exception
    {
        localeToLanguageTag(null);
    }

    @Test
    public void stringToLocale() throws Exception
    {
        assertEquals(Locale.GERMANY, languageTagToLocale("de-DE"));
        assertEquals(Locale.GERMANY, languageTagToLocale("de_DE"));
        assertEquals(Locale.GERMANY, languageTagToLocale("deutsch_DE"));
        assertEquals(Locale.GERMANY, languageTagToLocale("de_DEU"));
        assertEquals(Locale.GERMANY, languageTagToLocale("de_DEUTSCHLAND"));
        assertEquals(Locale.GERMAN, languageTagToLocale("de"));
        assertEquals(Locale.US, languageTagToLocale("en-US"));
        assertEquals(Locale.ENGLISH, languageTagToLocale("en"));
        assertEquals(Locale.getDefault(), languageTagToLocale(null));
        assertEquals(Locale.getDefault(), languageTagToLocale("und"));
        assertEquals(Locale.getDefault(), languageTagToLocale(""));
        assertEquals(new Locale(Locale.getDefault().getLanguage(), Locale.CHINA.getCountry()),
                     languageTagToLocale("und-CN"));
    }
}
