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
package de.cubeisland.engine.i18n;

import org.cubeengine.linguist.LinguistException;
import org.cubeengine.linguist.language.ClonedLanguage;
import org.cubeengine.linguist.loader.DefinitionLoadingException;
import org.cubeengine.linguist.language.Language;
import org.cubeengine.linguist.loader.LanguageLoader;
import org.cubeengine.linguist.language.SourceLanguage;
import org.cubeengine.linguist.loader.TranslationLoader;
import org.cubeengine.linguist.loader.TranslationLoadingException;
import org.cubeengine.linguist.translation.TranslationException;

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

    public final SourceLanguage getSourceLanguage()
    {
        return sourceLanguage;
    }

    public final TranslationLoader getTranslationLoader()
    {
        return tLoader;
    }

    public final LanguageLoader getLanguageLoader()
    {
        return lLoader;
    }

    public final Locale getDefaultLocale()
    {
        return defaultLocale;
    }

    public Language getLanguage(Locale locale) throws TranslationLoadingException, DefinitionLoadingException
    {
        if (locale == null)
        {
            throw new IllegalArgumentException("The locale must not be null!");
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
        return this.translate((String)null, toTranslate);
    }

    public String translate(Locale locale, String toTranslate)
    {
        return this.translate(locale, null, toTranslate);
    }

    public String translate(String context, String toTranslate)
    {
        return this.translate(this.getDefaultLocale(), context, toTranslate);
    }

    public String translate(Locale locale, String context, String toTranslate)
    {
        return this.translate0(locale, context, toTranslate, null, 0, false);
    }

    public String translateN(String singular, String plural, int n)
    {
        return this.translateN((String)null, singular, plural, n);
    }

    public String translateN(String context, String singular, String plural, int n)
    {
        return this.translateN(this.getDefaultLocale(), context, singular, plural, n);
    }

    public String translateN(Locale locale, String singular, String plural, int n)
    {
        return this.translateN(locale, null, singular, plural, n);
    }

    public String translateN(Locale locale, String context, String singular, String plural, int n)
    {
        return this.translate0(locale, context, singular, plural, n, true);
    }

    private String translate0(Locale locale, String context, String singular, String plural, int n, boolean isPlural)
    {
        try
        {
            String translated = this.translate0(locale, context, singular, plural, n, isPlural, true);
            if (translated == null || translated.length() == 0)
            {
                // Fallback to Default
                translated = this.translate0(this.getDefaultLocale(), context, singular, plural, n, isPlural, false);
            }
            if (translated == null || translated.length() == 0)
            {
                // Fallback to Source
                if (isPlural)
                {
                    translated = this.getSourceLanguage().getTranslation(context, singular, plural, n);
                }
                else
                {
                    translated = this.getSourceLanguage().getTranslation(context, singular);
                }
            }
            return translated;
        }
        catch (DefinitionLoadingException e)
        {
            //throw new LinguistException("", e);
        }
        catch (TranslationException e)
        {
            //throw new LinguistException("", e);
        }
        return null;
    }

    private String translate0(Locale locale, String context, String singular, String plural, int n, boolean isPlural, boolean fallbackToBaseLocale) throws DefinitionLoadingException, TranslationException
    {
        Language language = this.getLanguage(locale);
        if (language != null)
        {
            if (isPlural)
            {
                return language.getTranslation(context, singular, plural, n);
            }
            return language.getTranslation(context, singular);
        }
        else if (fallbackToBaseLocale && !locale.getLanguage().equalsIgnoreCase(locale.getCountry()))
        {
            // Search BaseLocale
            return this.translate0(new Locale(locale.getLanguage(), locale.getLanguage().toUpperCase()), context, singular, plural, n, isPlural, false);
        }
        return null;
    }
}
