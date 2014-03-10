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
package de.cubeisland.engine.i18n.language;

import de.cubeisland.engine.i18n.translation.TranslationContainer;

import java.util.Locale;

/**
 * This class is a generic language that loads its translations from files.
 */
public class NormalLanguage implements Language
{
    private final String name;
    private final String localName;
    private final Language parent;
    private final TranslationContainer messages;
    private final Locale locale;

    public NormalLanguage(LocaleConfig config, TranslationContainer messages, Language parent)
    {
        if (config.getLocale() == null)
        {
            throw new IllegalArgumentException("The locale must not be null!");
        }
        if (config.getName() == null)
        {
            throw new IllegalArgumentException("The name must not be null!");
        }
        if (config.getLocalName() == null)
        {
            throw new IllegalArgumentException("The local name must not be null!");
        }

        this.name = config.getName();
        this.localName = config.getLocalName();
        this.locale = config.getLocale();
        this.parent = parent;
        this.messages = messages;
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

    public String getTranslation(String singular)
    {
        String translation = this.messages.getSingular(singular);
        if (translation == null && parent != null)
        {
            translation = this.parent.getTranslation(singular);
        }
        return translation;
    }

    public String getTranslation(String plural, int n)
    {
        String translation = this.messages.getPlural(plural, n);
        if (translation == null && parent != null)
        {
            translation = this.parent.getTranslation(plural, n);
        }
        return translation;
    }

    public TranslationContainer getMessages()
    {
        return this.messages;
    }

    /**
     * Returns the language's parent
     *
     * @return the parent language
     */
    public Language getParent()
    {
        return this.parent;
    }

    @Override
    public int hashCode()
    {
        return this.locale.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof NormalLanguage) || getClass() != obj.getClass())
        {
            return false;
        }
        return this.locale.equals(((NormalLanguage)obj).locale);
    }
}
