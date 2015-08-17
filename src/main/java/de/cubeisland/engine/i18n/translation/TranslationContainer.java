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
package de.cubeisland.engine.i18n.translation;

import java.util.ArrayList;
import java.util.List;

public class TranslationContainer
{
    private final List<Translation> translations;

    public TranslationContainer()
    {
        this.translations = new ArrayList<Translation>();
    }

    public Translation addTranslation(Translation translation)
    {
        // load index of available translation
        int index = this.translations.indexOf(translation);
        if (index < 0)
        {
            // add completely new translation
            this.translations.add(translation);
            return null;
        }

        // override old translation and return it
        Translation old = this.translations.get(index);
        this.translations.set(index, translation);
        return old;
    }

    private Translation getTranslation0(String context, String singular)
    {
        for (Translation translation : this.translations)
        {
            if (context == null && translation.hasContext() || context != null && !context.equals(translation.getContext()))
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

    public String getTranslation(String context, String singular, String plural, int n) throws TranslationLoadingException
    {
        Translation translation = this.getTranslation0(context, singular);
        if (translation == null)
        {
            return null;
        }

        if (translation.isPluralTranslation() && !translation.getPluralId().equals(plural))
        {
            throw new TranslationLoadingException("The specified pluralId is different to the pluralId of the loaded translation.");
        }

        return translation.getTranslation(n);
    }

    public void putTranslation(String context, String singular, String plural, String translation, int n, int maxN) throws TranslationLoadingException
    {
        Translation trans = this.getTranslation0(context, singular);

        if (trans != null)
        {
            trans.setTranslation(translation, n);
            return;
        }

        if (plural == null && n == 0)
        {
            this.addTranslation(new SingularTranslation(context, singular, translation));
            return;
        }

        Translation pluralTranslation = new PluralTranslation(context, singular, plural, new String[maxN]);
        pluralTranslation.setTranslation(translation, n);
        this.addTranslation(pluralTranslation);
    }
}
