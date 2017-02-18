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

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.cubeengine.linguist.translation.PluralTranslation;
import org.cubeengine.linguist.translation.SingularTranslation;
import org.cubeengine.linguist.translation.Translation;
import org.cubeengine.linguist.translation.TranslationContainer;
import org.fedorahosted.tennera.jgettext.Catalog;
import org.fedorahosted.tennera.jgettext.Message;
import org.fedorahosted.tennera.jgettext.PoParser;

public class GettextLoader implements TranslationLoader
{
    private final PoParser parser;
    private final List<URL> poFiles;
    private final Charset charset;

    public GettextLoader(final Charset charset, final List<URL> poFiles)
    {
        this.charset = charset;
        this.poFiles = poFiles;
        this.parser = new PoParser();
    }

    public TranslationContainer loadTranslations(final TranslationContainer container,
                                                 final Locale locale) throws TranslationLoadingException
    {
        final Set<URL> loadFrom = new LinkedHashSet<URL>();
        for (final URL poFile : poFiles)
        {
            final String fileName = poFile.toString();
            if (fileName.endsWith(locale.getLanguage().toLowerCase() + "_" + locale.getCountry().toUpperCase() + ".po")
                || fileName.endsWith(locale.getLanguage().toLowerCase() + ".po"))
            {
                loadFrom.add(poFile);
            }
        }

        for (final URL url : loadFrom)
        {
            final Catalog catalog = this.parseCatalog(url);
            if (catalog == null)
            {
                continue;
            }

            for (final Message message : catalog)
            {
                final Translation translation;
                if (message.isPlural())
                {
                    final String[] translations = message.getMsgstrPlural().toArray(
                        new String[message.getMsgstrPlural().size()]);
                    translation = new PluralTranslation(message.getMsgctxt(), message.getMsgid(),
                                                        message.getMsgidPlural(), translations);
                }
                else
                {
                    translation = new SingularTranslation(message.getMsgctxt(), message.getMsgid(),
                                                          message.getMsgstr());
                }
                container.addTranslation(translation);
            }
        }
        return container;
    }

    private Catalog parseCatalog(final URL url) throws TranslationLoadingException
    {
        try
        {
            return this.parser.parseCatalog(url.openStream(), charset, true);
        }
        catch (final IOException e)
        {
            throw new TranslationLoadingException("An error occurred while parsing the catalog " + url, e);
        }
    }
}
