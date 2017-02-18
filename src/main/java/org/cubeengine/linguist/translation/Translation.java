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

/**
 * This interface defines a translation.
 */
public interface Translation
{
    /**
     * Returns whether this translation has a context.
     *
     * @return whether this translation has a context.
     */
    boolean hasContext();

    /**
     * Returns the context of this translation.
     *
     * @return context.
     */
    String getContext();

    /**
     * Returns the singular id of this translation.
     *
     * @return Singular id.
     */
    String getSingularId();

    /**
     * Returns the plural id of this translation.
     *
     * @return Plural id.
     */
    String getPluralId();

    /**
     * Returns the translation with the specified index.
     *
     * @param index The index of the translation.
     *
     * @return The translation string.
     *
     * @throws TranslationException if the translation can't be loaded.
     */
    String getTranslation(int index) throws TranslationException;

    /**
     * Sets the translation at the specified position.
     *
     * @param translation The translation
     * @param index       the index of the translation
     *
     * @throws TranslationException if the translation can't be set.
     */
    void setTranslation(String translation, int index) throws TranslationException;

    /**
     * Returns the count of translations.
     *
     * @return translation count
     */
    int getTranslationCount();

    /**
     * Returns whether this translation is a plural translation.
     *
     * @return whether it is a plural translation.
     */
    boolean isPluralTranslation();
}
