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
package org.cubeengine.i18n;

import java.util.Locale;

public class I18nUtil
{
    private I18nUtil()
    {
    }

    public static String localeToString(Locale locale)
    {
        if (locale == null)
        {
            throw new IllegalArgumentException("The locale must not be null!");
        }
        return locale.getLanguage().toLowerCase(Locale.US) + '_' + locale.getCountry().toUpperCase(Locale.US);
    }

    private static boolean mayBeRegionCode(String string)
    {
        return isNumeric(string) && Integer.parseInt(string) <= 999;
    }

    private static boolean isNumeric(String string)
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

    public static Locale stringToLocale(String string)
    {
        if (string == null)
        {
            return Locale.getDefault();
        }
        String localeString = string.trim();
        if (localeString.length() == 0)
        {
            return Locale.getDefault();
        }

        localeString = localeString.replace('-', '_').replaceAll("[^a-zA-Z0-9_]", "");
        String[] parts = localeString.split("_", 2);

        String language = parts[0];
        String country = "";

        // if the language code is longer than 3-alpha's
        if (language.length() > 3)
        {
            // strip it to a 2-alpha code
            language = language.substring(0, 2);
        }
        if (parts.length > 0)
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
