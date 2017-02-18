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

/**
 * A class with static i18n util methods.
 */
public final class I18nUtil
{
    private I18nUtil()
    {
    }

    /**
     * Returns whether the specified string is null or empty.
     *
     * @param string string to test
     *
     * @return whether the string is null or empty
     */
    public static boolean isNullOrEmpty(final String string)
    {
        return string == null || string.length() == 0;
    }

    /**
     * Returns a well-formed IETF BCP 47 language tag representing
     * this locale.
     * <p>
     * <p>If this <code>Locale</code> has a language, country, or
     * variant that does not satisfy the IETF BCP 47 language tag
     * syntax requirements, this method handles these fields as
     * described below:
     * <p>
     * <p><b>Language:</b> If language is empty, or not <a
     * href="#def_language" >well-formed</a> (for example "a" or
     * "e2"), it will be emitted as "und" (Undetermined).
     * <p>
     * <p><b>Country:</b> If country is not <a
     * href="#def_region">well-formed</a> (for example "12" or "USA"),
     * it will be omitted.
     *
     * @param locale The locale
     *
     * @return the language tag
     */
    public static String localeToLanguageTag(final Locale locale)
    {
        if (locale == null)
        {
            throw new IllegalArgumentException("The locale must not be null!");
        }
        final String delimiter = "-";
        final String language = locale.getLanguage().toLowerCase(Locale.US);
        final String country = locale.getCountry().toUpperCase(Locale.US);

        if (isNullOrEmpty(language))
        {
            if (isNullOrEmpty(country))
            {
                return "und";
            }
            return "und" + delimiter + country;
        }
        else if (isNullOrEmpty(country))
        {
            return language;
        }
        return language + delimiter + country;
    }

    private static boolean mayBeRegionCode(final String string)
    {
        return isNumeric(string) && Integer.parseInt(string) <= 999;
    }

    /**
     * Returns whether the specified string only contains digits.
     *
     * @param string string
     *
     * @return whether the specified string only contains digits
     */
    protected static boolean isNumeric(final String string)
    {
        if (string == null)
        {
            throw new IllegalArgumentException("The string must not be null!");
        }
        final int len = string.length();
        if (len == 0)
        {
            return false;
        }
        for (int i = 0; i < len; ++i)
        {
            if (!Character.isDigit(string.charAt(i)))
            {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a locale which was generated out of a language tag.
     *
     * @param languageTag The language Tag
     *
     * @return The matching {@link Locale}
     */
    public static Locale languageTagToLocale(final String languageTag)
    {
        if (languageTag == null)
        {
            return Locale.getDefault();
        }
        String localeString = languageTag.trim();

        localeString = localeString.replace('_', '-').replaceAll("[^a-zA-Z0-9-]", "");
        final String[] parts = localeString.split("-", 2);

        String language = parts[0];
        String country = "";

        // if the language code is longer than 3-alpha's
        if (language.length() > 3)
        {
            // strip it to a 2-alpha code
            language = language.substring(0, 2);
        }
        if (language.equals("und") || isNullOrEmpty(language))
        {
            final Locale defaultLocale = Locale.getDefault();
            language = defaultLocale.getLanguage();
            country = defaultLocale.getCountry();
        }
        if (parts.length > 1)
        {
            country = parts[1];
            if (country.length() > 2 && !mayBeRegionCode(country))
            {
                country = country.substring(0, 2);
            }
        }

        language = language.toLowerCase(Locale.US);
        country = country.toUpperCase(Locale.US);

        return new Locale(language, country);
    }
}
