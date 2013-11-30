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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * This class represents the source language.
 */
public final class SourceLanguage implements Language
{
    private final String code = "en_US";
    private final Locale locale = Locale.US;
    private final String name = "English";
    private final String localName = "English";
    private final Map<String, String> messages = new HashMap<String, String>();

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

    public String getTranslation(String message)
    {
        String translation = this.messages.get(message);
        if (translation == null)
        {
            // TODO this.messages.put(message, translation = ChatFormat.parseFormats(message));
        }

        return translation;
    }

    public Map<String, String> getMessages()
    {
        return new HashMap<String, String>(this.messages);
    }

    public boolean equals(Locale locale)
    {
        return this.locale.equals(locale);
    }

    @Override
    public int hashCode()
    {
        return this.code.hashCode();
    }

    public void clean()
    {
        this.messages.clear();
    }
}
