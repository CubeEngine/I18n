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
import org.cubeengine.linguist.translation.TranslationContainer;
import org.cubeengine.linguist.translation.TranslationException;

/**
 * This interface represents a language containing translations.
 */
public interface Language
{
    /**
     * Returns the language's locale
     *
     * @return a locale
     */
    Locale getLocale();

    /**
     * Return the language's name
     *
     * @return the name
     */
    String getName();

    /**
     * Returns the language's local name
     *
     * @return the local name
     */
    String getLocalName();

    /**
     * Returns the language's parent
     *
     * @return the parent language
     */
    Language getParent();

    /**
     * Returns the language's definition
     *
     * @return the language definition
     */
    LanguageDefinition getLanguageDefinition();

    /**
     * Gets a singular translation from this language
     *
     * @param context  the context of the message
     * @param singular the message
     *
     * @return the translation or null
     */
    String getTranslation(String context, String singular) throws TranslationException;

    /**
     * Gets a plural translation from this language based on the amount
     *
     * @param context  the context of the message
     * @param singular the message
     * @param n        the amount
     *
     * @return the translated message or null
     */
    String getTranslation(String context, String singular, String plural, int n) throws TranslationException;

    /**
     * Returns a map of all translations of the given category
     *
     * @return all translations of the category
     */
    TranslationContainer getMessages();
}
