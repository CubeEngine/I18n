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
package de.cubeisland.engine.i18n.loader;

import de.cubeisland.engine.i18n.TranslationContainer;
import de.cubeisland.engine.i18n.TranslationLoader;
import org.fedorahosted.tennera.jgettext.Catalog;
import org.fedorahosted.tennera.jgettext.Message;
import org.fedorahosted.tennera.jgettext.PoParser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GettextLoader implements TranslationLoader
{
    private final PoParser parser;
    private final File baseDirectory;

    public GettextLoader(File baseDirectory)
    {
        this.baseDirectory = baseDirectory;
        this.parser = new PoParser();
    }

    public TranslationContainer loadTranslations(Locale locale)
    {
        Map<String, String> singularMessages = new HashMap<String, String>();
        Map<String, List<String>> pluralMessages = new HashMap<String, List<String>>();

        File localeFolder = new File(this.baseDirectory, locale.getLanguage().toLowerCase() + "_" + locale.getCountry().toLowerCase());

        if (!localeFolder.exists() || !localeFolder.isDirectory())
        {
            return null;
        }

        File[] catalogFiles = localeFolder.listFiles();
        if (catalogFiles != null)
        {
            for (File catalogFile : catalogFiles)
            {
                if (!catalogFile.isFile())
                {
                    continue;
                }
                try
                {
                    Catalog catalog = this.parser.parseCatalog(catalogFile);

                    for (Message m : catalog)
                    {
                        singularMessages.put(m.getMsgid(), m.getMsgstr());
                        pluralMessages.put(m.getMsgidPlural(), m.getMsgstrPlural());
                    }
                }
                catch (IOException e)
                {
                    throw new RuntimeException("Failed to load the message catalog from '" + catalogFile.getAbsolutePath() + "'", e);
                }
            }
        }

        return new TranslationContainer(singularMessages, pluralMessages);
    }
}
