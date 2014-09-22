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

import de.cubeisland.engine.i18n.language.DefinitionLoadingException;
import de.cubeisland.engine.i18n.language.LanguageDefinition;
import de.cubeisland.engine.i18n.language.LanguageLoader;
import de.cubeisland.engine.i18n.plural.NotOneExpr;
import de.cubeisland.engine.i18n.plural.PluralExpr;

import java.util.Locale;

public class I18nLanguageLoader extends LanguageLoader
{
    private GermanLanguageDefinition germanLanguageDefinition = new GermanLanguageDefinition();

    @Override
    public LanguageDefinition loadDefinition(Locale locale) throws DefinitionLoadingException
    {
        if (Locale.GERMANY.equals(locale))
        {
            return germanLanguageDefinition;
        }
        return null;
    }

    public class GermanLanguageDefinition implements LanguageDefinition
    {
        public Locale getLocale()
        {
            return Locale.GERMANY;
        }

        public String getName()
        {
            return "German";
        }

        public String getLocalName()
        {
            return "Deutsch";
        }

        public Locale getParent()
        {
            return Locale.GERMAN;
        }

        public Locale[] getClones()
        {
            return null;
        }

        public int getPluralCount()
        {
            return 2;
        }

        public PluralExpr getPluralExpression()
        {
            return new NotOneExpr();
        }
    }
}
