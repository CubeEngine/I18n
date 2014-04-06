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
package de.cubeisland.engine.i18n.translation;

import java.util.HashMap;
import java.util.Map;

public class TranslationContainer
{
    private final Map<String, String> singularMessages;
    private final Map<String, String[]> pluralMessages;

    public TranslationContainer(Map<String, String> singularMessages, Map<String, String[]> pluralMessages)
    {
        this.singularMessages = singularMessages;
        this.pluralMessages = pluralMessages;
    }

    public TranslationContainer()
    {
        this(new HashMap<String, String>(), new HashMap<String, String[]>());
    }

    public String getSingular(String message)
    {
        return this.singularMessages.get(message);
    }

    public String getPlural(String message, int index)
    {
        String[] translations = this.pluralMessages.get(message);
        if (translations != null)
        {
            String translation = translations[index];
            if (translation != null)
            {
                return translation;
            }
        }
        return null;
    }

    public void merge(Map<String, String> singularMessages, Map<String, String[]> pluralMessages)
    {
        singularMessages.keySet().removeAll(this.singularMessages.keySet());
        pluralMessages.keySet().removeAll(this.pluralMessages.keySet());
        this.putAll(singularMessages, pluralMessages);
    }

    public void putAll(Map<String, String> singularMessages, Map<String, String[]> pluralMessages)
    {
        this.singularMessages.putAll(singularMessages);
        this.pluralMessages.putAll(pluralMessages);
    }

    public void putSingular(String singular, String result)
    {
        this.singularMessages.put(singular, result);
    }

    public void putPlural(String plural, String result, int n, int maxN)
    {
        String[] translations = this.pluralMessages.get(plural);
        if (translations == null)
        {
            translations = new String[maxN];
            this.pluralMessages.put(plural, translations);
        }
        translations[n] = result;
    }
}
