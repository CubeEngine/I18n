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
package org.cubeengine.i18n.language;

import org.cubeengine.i18n.translation.TranslationContainer;

import java.util.Locale;

/**
 * This class is a generic language that loads its translations from files.
 */
public class NormalLanguage implements Language
{
    private final Language parent;
    protected final TranslationContainer messages;
    protected final LanguageDefinition definition;

    public NormalLanguage(LanguageDefinition definition, TranslationContainer messages, Language parent)
    {
        if (definition.getLocale() == null)
        {
            throw new IllegalArgumentException("The locale must not be null!");
        }
        if (definition.getName() == null)
        {
            throw new IllegalArgumentException("The name must not be null!");
        }
        if (definition.getLocalName() == null)
        {
            throw new IllegalArgumentException("The local name must not be null!");
        }
        this.definition = definition;
        this.parent = parent;
        this.messages = messages;

    }

    public Locale getLocale()
    {
        return this.definition.getLocale();
    }

    public String getName()
    {
        return this.definition.getName();
    }

    public String getLocalName()
    {
        return this.definition.getLocalName();
    }

    public LanguageDefinition getLanguageDefinition()
    {
        return this.definition;
    }

    public String getTranslation(String singular)
    {
        String translation = this.messages.getSingular(singular);
        if (translation == null && parent != null)
        {
            translation = this.parent.getTranslation(singular);
        }
        return translation;
    }

    public String getTranslation(String singular, String plural, int n)
    {
        int index = this.getIndex(n);
        if (index == 0)
        {
            return getTranslation(singular);
        }
        String translation = this.messages.getPlural(plural, index - 1);
        if (translation == null && parent != null)
        {
            translation = this.parent.getTranslation(singular, plural, n);
        }
        return translation;
    }

    public TranslationContainer getMessages()
    {
        return this.messages;
    }

    public Language getParent()
    {
        return this.parent;
    }

    @Override
    public int hashCode()
    {
        return this.getLocale().hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof NormalLanguage) || getClass() != obj.getClass())
        {
            return false;
        }
        return this.getLocale().equals(((NormalLanguage)obj).getLocale());
    }

    protected final int getIndex(int n)
    {
        return this.definition.getPluralExpression().evaluate(n);
    }
}
