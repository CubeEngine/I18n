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

import de.cubeisland.engine.i18n.TranslationLoadingException;
import de.cubeisland.engine.i18n.translation.TranslationContainer;
import de.cubeisland.engine.i18n.translation.TranslationLoader;
import org.fedorahosted.tennera.jgettext.Catalog;
import org.fedorahosted.tennera.jgettext.Message;
import org.fedorahosted.tennera.jgettext.PoParser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class GettextLoader implements TranslationLoader
{
    private final PoParser parser;
    private final List<URI> searchDirectories;
    private final Charset charset;

    public GettextLoader(Charset charset, List<URI> searchDirectories)
    {
        this.charset = charset;
        this.searchDirectories = searchDirectories;
        this.parser = new PoParser();
    }

    public TranslationContainer loadTranslations(TranslationContainer container, Locale locale) throws TranslationLoadingException
    {
        Map<String, String> singularMessages = new HashMap<String, String>();
        Map<String, List<String>> pluralMessages = new HashMap<String, List<String>>();
        for (URI searchDirectory : searchDirectories)
        {
            URI uri = searchDirectory.resolve(locale.getLanguage().toLowerCase() + "_" + locale.getCountry().toUpperCase() + ".po");
            Catalog catalog = this.parseCatalog(uri);
            if (catalog == null)
            {
                catalog = this.parseCatalog(searchDirectory.resolve(locale.getLanguage().toLowerCase() + ".po"));
            }
            if (catalog != null)
            {
                for (Message message : catalog)
                {
                    singularMessages.put(message.getMsgid(), message.getMsgstr());
                    pluralMessages.put(message.getMsgidPlural(), message.getMsgstrPlural());
                }
            }
        }
        container.merge(singularMessages, pluralMessages);
        return container;
    }

    private Catalog parseCatalog(URI uri) throws TranslationLoadingException
    {
        try
        {
            return this.parser.parseCatalog(uri.toURL().openStream(), charset, true);
        }
        catch (MalformedURLException e)
        {
            throw new TranslationLoadingException(e);
        }
        catch (IOException ignore)
        {
            return null;
        }
    }
}
