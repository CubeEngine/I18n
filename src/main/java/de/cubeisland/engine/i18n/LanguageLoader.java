package de.cubeisland.engine.i18n;

import de.cubeisland.engine.i18n.language.Language;

import java.util.Locale;

public interface LanguageLoader
{
    Language loadLanguage(Locale locale);
}
