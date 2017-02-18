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

import java.util.LinkedList;
import java.util.List;
import org.cubeengine.linguist.loader.TranslationLoadingException;

public class TranslationContainer
{
    private final List<Translation> translations;

    public TranslationContainer()
    {
        this.translations = new LinkedList<Translation>();
    }

    /**
     * Adds a {@link Translation} to the container.
     *
     * @param translation The {@link Translation} to add.
     *
     * @return {@code null} if the translation is a completely new one or the old {@link Translation} which was overwritten.
     */
    public Translation addTranslation(final Translation translation)
    {
        // load index of available translation
        final int index = this.translations.indexOf(translation);
        if (index < 0)
        {
            // add completely new translation
            this.translations.add(translation);
            return null;
        }

        // override old translation and return it
        final Translation old = this.translations.get(index);
        this.translations.set(index, translation);
        return old;
    }

    /**
     * Gets the {@link Translation} string which matches the parameters. The method already invokes the
     * {@link Translation#getTranslation(int)} method with the specified index.
     *
     * @param context  The context of the translation.
     * @param singular The singular id of the translation
     * @param plural   The plural id
     * @param n        The plural index of the translation
     */
    public String getTranslation(final String context, final String singular, final String plural,
                                 final int n) throws TranslationException
    {
        final Translation translation = this.getTranslation0(context, singular);
        if (translation == null)
        {
            return null;
        }

        if (translation.isPluralTranslation() && !translation.getPluralId().equals(plural))
        {
            throw new TranslationLoadingException(
                "The specified pluralId is different to the pluralId of the loaded translation.");
        }

        return translation.getTranslation(n);
    }

    /**
     * Changes a translation String of a {@link Translation} or adds a completely new one.
     *
     * @param context           The context of the {@link Translation}.
     * @param singular          The singular id of the {@link Translation}.
     * @param plural            The plural id of the {@link Translation}.
     * @param translationString The translation string.
     * @param n                 The index of the translation.
     * @param maxN              The maximum number of elements if it's a plural translation.
     */
    public void putTranslation(final String context, final String singular, final String plural,
                               final String translationString, final int n, final int maxN) throws TranslationException
    {
        final Translation translation = this.getTranslation0(context, singular);

        if (translation != null)
        {
            translation.setTranslation(translationString, n);
            return;
        }

        if (plural == null && n == 0)
        {
            this.addTranslation(new SingularTranslation(context, singular, translationString));
            return;
        }

        final Translation pluralTranslation = new PluralTranslation(context, singular, plural, new String[maxN]);
        pluralTranslation.setTranslation(translationString, n);
        this.addTranslation(pluralTranslation);
    }

    /**
     * Helper method searching for a {@link Translation} in the translation list.
     *
     * @param context  The context of the translation.
     * @param singular The singular id.
     *
     * @return The matching {@link Translation}.
     */
    private Translation getTranslation0(final String context, final String singular)
    {
        for (final Translation translation : this.translations)
        {
            if (context == null && translation.hasContext() || context != null && !context.equals(
                translation.getContext()))
            {
                continue;
            }

            if (singular.equals(translation.getSingularId()))
            {
                return translation;
            }
        }
        return null;
    }
}
