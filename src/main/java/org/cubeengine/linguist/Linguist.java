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
package org.cubeengine.linguist;

import java.util.Locale;
import org.cubeengine.linguist.language.Language;
import org.cubeengine.linguist.language.SourceLanguage;
import org.cubeengine.linguist.loader.LanguageLoader;
import org.cubeengine.linguist.loader.TranslationLoader;

/**
 * DESC.
 */
public interface Linguist
{
    SourceLanguage getSourceLanguage();

    TranslationLoader getTranslationLoader();

    LanguageLoader getLanguageLoader();

    Language getLanguage(Locale locale) throws LinguistException;

    Locale getDefaultLocale();

    Language getDefaultLanguage() throws LinguistException;

    String translate(String context, String singularId);

    String translate(Locale locale, String context, String singularId);

    String translateN(String context, String singularId, String pluralId);

    String translateN(Locale locale, String context, String singularId, String pluralId);
}