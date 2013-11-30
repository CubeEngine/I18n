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
 * This class is a generic language that loads its translations from files.
 */
public class NormalLanguage implements Language
{
    private final String name;
    private final String localName;
    private final Language parent;
    private final Map<String, String> messages;
    private final Locale locale;

    public NormalLanguage(LocaleConfig config, Map<String, String> messages, Language parent)
    {
        assert config.locale != null: "The code must not be null!";
        assert config.name != null: "The name must not be null!";
        assert config.localName != null: "The local name must not be null!";

        this.name = config.name;
        this.localName = config.localName;
        this.locale = config.locale;
        this.parent = parent;
        this.messages = new HashMap<String, String>(messages);
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

    /**
     * This method adds a map of translations to a category
     *
     * @param messages the translations
     */
    public void addMessages(Map<String, String> messages)
    {
        if (messages == null)
        {
            throw new NullPointerException("The messages must not be null!");
        }

        this.messages.putAll(messages);
    }

    public String getTranslation(String message)
    {
        String translation = this.messages.get(message);
        if (translation == null && parent != null)
        {
            translation = this.parent.getTranslation(message);
        }
        return translation;
    }

    public Map<String, String> getMessages()
    {
        return new HashMap<String, String>(this.messages);
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

    @SuppressWarnings("unchecked")
    private synchronized Map<String, String> loadMessages(String cat)
    {
        /*
        try
        {
            final File messagesFile = new File(this.messageDir, cat + ".json");
            Map<String, String> moduleMessages = this.objectMapper.readValue(messagesFile, Map.class);
            moduleMessages = this.updateMessages(messagesFile, moduleMessages);

            for (Map.Entry<String, String> translation : moduleMessages.entrySet())
            {
                if (!this.messages.containsKey(translation.getKey()))
                {
                    this.messages.put(translation.getKey(), ChatFormat.parseFormats(translation.getValue()));
                }
            }
            return moduleMessages;
        }
        catch (FileNotFoundException ignored)
        {
            this.core.getLog().warn("The translation category " + cat + " was not found for the language ''" + this.code + "'' !");
        }
        catch (IOException e)
        {
            this.core.getLog().error(String.valueOf(e), e);
        }*/
        return null;
    }

    public boolean equals(Locale locale)
    {
        return this.locale.equals(locale);
    }

    @Override
    public int hashCode()
    {
        return this.locale.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || getClass() != obj.getClass() || !(obj instanceof NormalLanguage))
        {
            return false;
        }
        return this.locale.equals(((NormalLanguage)obj).locale);
    }
}
