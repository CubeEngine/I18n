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
package de.cubeisland.engine.i18n;

import de.cubeisland.engine.i18n.language.ClonedLanguage;
import de.cubeisland.engine.i18n.language.Language;
import de.cubeisland.engine.i18n.language.SourceLanguage;
import de.cubeisland.engine.i18n.translation.TranslationLoader;

import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class I18nService
{
    private final SourceLanguage sourceLanguage;
    private final TranslationLoader tLoader;
    private final LanguageLoader lLoader;
    private final Locale defaultLocale;

    private final Map<Locale, Language> languages = new HashMap<Locale, Language>();

    public I18nService(SourceLanguage source, TranslationLoader tLoader, LanguageLoader lLoader, Locale defaultLocale)
    {
        this.sourceLanguage = source;
        this.tLoader = tLoader;
        this.lLoader = lLoader;
        this.defaultLocale = defaultLocale;

        this.languages.put(this.getSourceLanguage().getLocale(), this.getSourceLanguage());
    }

    public SourceLanguage getSourceLanguage()
    {
        return sourceLanguage;
    }

    public TranslationLoader getTranslationLoader()
    {
        return tLoader;
    }

    public LanguageLoader getLanguageLoader()
    {
        return lLoader;
    }

    public Locale getDefaultLocale()
    {
        return defaultLocale;
    }

    public Language getLanguage(Locale locale) throws TranslationLoadingException, DefinitionLoadingException
    {
        if (locale == null)
        {
            throw new NullPointerException("The locale must not be null!");
        }
        Language result = this.languages.get(locale);
        if (result == null)
        {
            Language language = this.lLoader.loadLanguage(this, locale);
            if (language == null)
            {
                return null;
            }
            result = language;
            if (result instanceof ClonedLanguage)
            {
                Language original = ((ClonedLanguage)result).getOriginal();
                this.languages.put(original.getLocale(), original);
            }
            this.languages.put(locale, result);
        }
        return result;
    }

    public Language getDefaultLanguage() throws TranslationLoadingException, DefinitionLoadingException
    {
        Language language = this.getLanguage(this.defaultLocale);
        if (language == null)
        {
            language = this.getSourceLanguage();
        }
        return language;
    }

    public Collection<Language> getLoadedLanguages()
    {
        return this.languages.values();
    }

    public String translate(String toTranslate)
    {
        return this.translate(this.getDefaultLocale(), toTranslate);
    }

    public String translate(Locale locale, String toTranslate)
    {
        return this.translateN(locale, toTranslate, 0);
    }

    public String translateN(String toTranslate, int n)
    {
        return this.translateN(this.getDefaultLocale(), toTranslate, n);
    }

    public String translateN(Locale locale, String toTranslate, int n)
    {
        try
        {
            String translated = this.translate0(locale, toTranslate, n, true);
            if (translated == null)
            {
                // Fallback to Default
                translated = this.translate0(this.getDefaultLocale(), toTranslate, n, false);
            }
            if (translated == null)
            {
                // Fallback to Source
                if (n == 0)
                {
                    translated = this.getSourceLanguage().getTranslation(toTranslate);
                }
                else
                {
                    translated = this.getSourceLanguage().getTranslation(toTranslate, n);
                }
            }
            return translated;
        }
        catch (DefinitionLoadingException e)
        {
            throw new TranslationException(e);
        }
        catch (TranslationLoadingException e)
        {
            throw new TranslationException(e);
        }
    }

    private String translate0(Locale locale, String toTranslate, int n, boolean fallbackToBaseLocale) throws DefinitionLoadingException, TranslationLoadingException
    {
        Language language = this.getLanguage(locale);
        if (language != null)
        {
            if (n == 0)
            {
                return language.getTranslation(toTranslate);
            }
            return language.getTranslation(toTranslate, n);
        }
        else if (fallbackToBaseLocale && !locale.getLanguage().toLowerCase().equals(locale.getCountry().toLowerCase()))
        {
            // Search BaseLocale
            return this.translate0(new Locale(locale.getLanguage(), locale.getLanguage().toUpperCase()), toTranslate, n, false);
        }
        return null;
    }
}
