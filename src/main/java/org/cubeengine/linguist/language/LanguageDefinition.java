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
package org.cubeengine.linguist.language;

import java.util.Locale;
import org.cubeengine.linguist.plural.PluralExpr;

/**
 * This configuration is used to parse the language configurations.
 */
public interface LanguageDefinition
{
    /**
     * Returns the {@link Locale} of the language.
     *
     * @return {@link Locale}
     */
    Locale getLocale();

    /**
     * Returns the name of the language in english.
     *
     * @return name in english
     */
    String getName();

    /**
     * Returns the name in the locale language.
     *
     * @return name in locale language
     */
    String getLocalName();

    /**
     * Returns the parent {@link Locale} of this language.
     *
     * @return parent
     */
    Locale getParent();

    /**
     * Returns an array of {@link Locale}s defining cloned languages.
     *
     * @return clones
     */
    Locale[] getClones();

    /**
     * Returns the plural count of this language.
     *
     * @return plural count
     */
    int getPluralCount();

    /**
     * Returns the plural expression.
     *
     * @return plural expression
     */
    PluralExpr getPluralExpression();
}
