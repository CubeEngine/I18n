/**
 * The MIT License
 * Copyright (c) ${project.inceptionYear} Cube Island
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
package de.cubeisland.engine.i18n.loader;

import de.cubeisland.engine.i18n.translation.TranslationContainer;
import de.cubeisland.engine.i18n.translation.TranslationLoader;
import de.cubeisland.engine.i18n.translation.TranslationLoadingException;
import org.fedorahosted.tennera.jgettext.Catalog;
import org.fedorahosted.tennera.jgettext.Message;
import org.fedorahosted.tennera.jgettext.PoParser;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class GettextLoader implements TranslationLoader
{
    private final PoParser parser;
    private final List<URL> poFiles;
    private final Charset charset;

    public GettextLoader(Charset charset, List<URL> poFiles)
    {
        this.charset = charset;
        this.poFiles = poFiles;
        this.parser = new PoParser();
    }

    public TranslationContainer loadTranslations(TranslationContainer container, Locale locale) throws TranslationLoadingException
    {
        Map<String, String> singularMessages = new HashMap<String, String>();
        Map<String, String[]> pluralMessages = new HashMap<String, String[]>();
        Set<URL> loadFrom = new LinkedHashSet<URL>();
        for (URL poFile : poFiles)
        {
            String fileName = poFile.toString();
            if (fileName.endsWith(locale.getLanguage().toLowerCase() + "_" + locale.getCountry().toUpperCase() + ".po")
                    || fileName.endsWith(locale.getLanguage().toLowerCase() + ".po"))
            {
                loadFrom.add(poFile);
            }
        }
        for (URL url : loadFrom)
        {
            Catalog catalog = this.parseCatalog(url);
            if (catalog != null)
            {
                for (Message message : catalog)
                {
                    singularMessages.put(message.getMsgid(), message.getMsgstr());
                    pluralMessages.put(message.getMsgidPlural(), message.getMsgstrPlural().toArray(new String[message.getMsgstrPlural().size()]));
                }
            }
            container.merge(singularMessages, pluralMessages);
        }
        return container;
    }

    private Catalog parseCatalog(URL url) throws TranslationLoadingException
    {
        try
        {
            return this.parser.parseCatalog(url.openStream(), charset, true);
        }
        catch (IOException e)
        {
            throw new TranslationLoadingException(e);
        }
    }
}
