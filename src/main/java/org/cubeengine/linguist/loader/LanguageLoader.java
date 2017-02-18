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
package org.cubeengine.linguist.loader;

import de.cubeisland.engine.i18n.I18nService;
import org.cubeengine.linguist.language.ClonedLanguage;
import org.cubeengine.linguist.language.Language;
import org.cubeengine.linguist.language.LanguageDefinition;
import org.cubeengine.linguist.language.NormalLanguage;
import org.cubeengine.linguist.translation.TranslationContainer;

import java.util.Locale;

public abstract class LanguageLoader
{
    public Language loadLanguage(I18nService service, Locale locale) throws TranslationLoadingException, DefinitionLoadingException
    {
        LanguageDefinition definition = this.loadDefinition(locale);
        if (definition == null)
        {
            return null;
        }
        if (definition.getLocale().equals(locale))
        {
            Language parent = null;
            if (definition.getParent() != null)
            {
                parent = service.getLanguage(definition.getParent());
            }
            // Main Locale of Configuration
            return new NormalLanguage(definition, service.getTranslationLoader().loadTranslations(new TranslationContainer(), locale), parent);
        }
        else
        {
            // Cloned Locale of Configuration -> Get main Language first
            Language mainLanguage = service.getLanguage(definition.getLocale());
            if (mainLanguage != null)
            {
                // Create Clone
                return new ClonedLanguage(locale, mainLanguage);
            }
            // else couldnt load main language
        }
        return null;
    }

    public abstract LanguageDefinition loadDefinition(Locale locale) throws DefinitionLoadingException;
}
