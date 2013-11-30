/**
 * This file is part of CubeEngine.
 * CubeEngine is licensed under the GNU General Public License Version 3.
 *
 * CubeEngine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CubeEngine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CubeEngine.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.cubeisland.engine.i18n;

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
