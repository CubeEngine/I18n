/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2013 CubeEngineDev
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package de.cubeisland.engine.i18n.translation;

public class PluralTranslation extends AbstractTranslation
{
    private final String pluralId;
    private final String[] translations;

    public PluralTranslation(String context, String singularId, String pluralId, String[] translations)
    {
        super(context, singularId);

        this.pluralId = pluralId;
        this.translations = translations;
    }

    public String getPluralId()
    {
        return this.pluralId;
    }

    public String getTranslation(int n) throws TranslationLoadingException
    {
        if (n < 0 || n > this.translations.length)
        {
            throw new TranslationLoadingException("The PluralTranslation doesn't have a translation with the index " + n + ".");
        }

        return this.translations[n];
    }

    public void setTranslation(String translation, int n) throws TranslationLoadingException
    {
        if (n < 0 || n > this.translations.length)
        {
            throw new TranslationLoadingException("The PluralTranslation doesn't have a translation with the index " + n + ".");
        }

        this.translations[n] = translation;
    }

    public int getTranslationCount()
    {
        return this.translations.length;
    }

    public boolean isPluralTranslation()
    {
        return true;
    }
}
