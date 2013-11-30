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

import java.util.Locale;
import java.util.Map;

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
            throw new NullPointerException("The code must not be null!");
        }
        if (original == null)
        {
            throw new NullPointerException("The original must not be null!");
        }

        this.locale = locale;
        this.original = original;
    }

    public static ClonedLanguage clone(Locale locale, Language original)
    {
        try
        {
            return new ClonedLanguage(locale, original);
        }
        catch (IllegalArgumentException e)
        {
            return null;
        }
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

    public String getTranslation(String message)
    {
        return this.original.getTranslation(message);
    }

    public Map<String, String> getMessages()
    {
        return this.original.getMessages();
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
