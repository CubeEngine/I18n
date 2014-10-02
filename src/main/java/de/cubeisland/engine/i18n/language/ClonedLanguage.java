/**
 * The MIT License
 * Copyright (c) ${project.inceptionYear} Cube Island
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
package de.cubeisland.engine.i18n.language;

import de.cubeisland.engine.i18n.translation.TranslationContainer;

import java.util.Locale;

/**
 * This class represents a clone of another language.
 */
public class ClonedLanguage implements Language
{
    private final Locale locale;
    private final Language original;

    public ClonedLanguage(Locale locale, Language original)
    {
        if (locale == null)
        {
            throw new IllegalArgumentException("The code must not be null!");
        }
        if (original == null)
        {
            throw new IllegalArgumentException("The original must not be null!");
        }

        this.locale = locale;
        this.original = original;
    }

    public String getName()
    {
        return this.original.getName();
    }

    public Locale getLocale()
    {
        return this.locale;
    }

    public String getLocalName()
    {
        return this.original.getLocalName();
    }

    public Language getParent()
    {
        return this.original.getParent();
    }

    public LanguageDefinition getLanguageDefinition()
    {
        return this.original.getLanguageDefinition();
    }

    public String getTranslation(String singular)
    {
        return this.original.getTranslation(singular);
    }

    public String getTranslation(String plural, int n)
    {
        return this.original.getTranslation(plural, n);
    }

    public TranslationContainer getMessages()
    {
        return this.original.getMessages();
    }

    @Override
    public int hashCode()
    {
        return this.locale.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (!(obj instanceof ClonedLanguage) || getClass() != obj.getClass())
        {
            return false;
        }
        return this.locale.equals(((ClonedLanguage)obj).locale);
    }

    /**
     * This method returns the language cloned by this language
     *
     * @return the original
     */
    public Language getOriginal()
    {
        return this.original;
    }
}
