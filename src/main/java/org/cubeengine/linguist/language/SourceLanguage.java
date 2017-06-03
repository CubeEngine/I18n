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

import org.cubeengine.linguist.plural.NotOneExpr;
import org.cubeengine.linguist.plural.PluralExpr;
import org.cubeengine.linguist.translation.TranslationContainer;
import org.cubeengine.linguist.loader.TranslationLoadingException;
import org.cubeengine.linguist.plural.PluralExpressionEvaluationException;
import org.cubeengine.linguist.translation.TranslationException;

import java.util.Locale;

/**
 * This class represents the source language.
 */
public final class SourceLanguage extends NormalLanguage
{
    public static final SourceLanguage EN_US = new SourceLanguage(Locale.US, "English", 2, new NotOneExpr());

    public SourceLanguage(Locale locale, String name, int pluralCount, PluralExpr pluralExpression)
    {
        this(locale, name, name, pluralCount, pluralExpression);
    }

    public SourceLanguage(Locale locale, String name, String localName, int pluralCount, PluralExpr pluralExpression)
    {
        super(new SourceLanguageDefinition(locale, name, localName, pluralCount, pluralExpression), new TranslationContainer(), null);
    }

    public String getTranslation(String context, String singular) throws TranslationException
    {
        // TODO preprocessor
        String result = this.messages.getTranslation(context, singular, null, 0);
        if (result == null)
        {
            result = singular;
            this.messages.putTranslation(context, singular, null, result, 0, 1);
        }
        return result;
    }

    public String getTranslation(final String context, final String singular, final String plural, final int n) throws TranslationException
    {
        // TODO preprocessor
        final int index;
        try
        {
            index = this.getIndex(n);
        }
        catch (final PluralExpressionEvaluationException e)
        {
            throw new TranslationLoadingException("Couldn't load the index of the message.", e);
        }

        String result = this.messages.getTranslation(context, singular, plural, index);
        if (result == null)
        {
            result = index == 0 ? singular : plural;
            this.messages.putTranslation(context, singular, plural, result, index, this.definition.getPluralCount());
        }
        return result;
    }

    public TranslationContainer getMessages()
    {
        return this.messages;
    }

    public Language getParent()
    {
        return null;
    }

    public LanguageDefinition getLanguageDefinition()
    {
        return this.definition;
    }

    private static class SourceLanguageDefinition implements LanguageDefinition
    {
        private static final Locale[] NO_CLONES = new Locale[0];

        private Locale locale;
        private String name;
        private String localName;
        private int pluralCount;
        private PluralExpr pluralExpression;

        private SourceLanguageDefinition(Locale locale, String name, String localName, int pluralCount, PluralExpr pluralExpression)
        {
            this.locale = locale;
            this.name = name;
            this.localName = localName;
            this.pluralCount = pluralCount;
            this.pluralExpression = pluralExpression;
        }

        public Locale getLocale()
        {
            return this.locale;
        }

        public String getName()
        {
            return this.name;
        }

        public String getLocalName()
        {
            return this.localName;
        }

        public Locale getParent()
        {
            return null;
        }

        public Locale[] getClones()
        {
            return NO_CLONES;
        }

        public int getPluralCount()
        {
            return pluralCount;
        }

        public PluralExpr getPluralExpression()
        {
            return pluralExpression;
        }
    }
}
