/*
 * The MIT License
 * Copyright © 2013 Cube Island
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
package org.cubeengine.i18n.loader;

import org.cubeengine.i18n.translation.TranslationContainer;
import org.cubeengine.i18n.translation.TranslationLoader;
import org.cubeengine.i18n.translation.TranslationLoadingException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GettextLoader implements TranslationLoader
{
    private final List<URL> poFiles;
    private final Charset charset;

    private static Pattern bigPattern = Pattern.compile("(msgid (\".*\"\\n)+)((msgstr (\".*\"\\n)+)|(msgid_plural (\".*\"\\n)+(msgstr\\[\\d+] (\".*\"\\n)+)+))");
    private static Pattern pluralPattern = Pattern.compile("(msgid ((\".*\"\\n)+))msgid_plural ((\".*\"\\n)+)((msgstr\\[\\d+] ((\".*\"\\n)+))+)");
    private static Pattern pluralTPattern = Pattern.compile("msgstr\\[\\d+] ((\".*\"\\n)+)");
    private static Pattern singularPattern = Pattern.compile("(msgid ((\".*\"\\n)+))msgstr ((\".*\"\\n)+)");

    public GettextLoader(Charset charset, List<URL> poFiles)
    {
        this.charset = charset;
        this.poFiles = poFiles;
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
            try
            {
                StringBuilder sb = new StringBuilder();

                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), this.charset));
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                {
                    sb.append(inputLine).append("\n");
                }
                in.close();

                String content = sb.toString();
                parseTranslations(content, singularMessages, pluralMessages);
            }
            catch (Exception e)
            {
                throw new TranslationLoadingException(e);
            }

            container.merge(singularMessages, pluralMessages);
        }
        return container;
    }

    public void parseTranslations(String lines, Map<String, String> singular, Map<String, String[]> plural)
    {
        Matcher matcher = bigPattern.matcher(lines);
        while (matcher.find())
        {
            String group = matcher.group();
            Matcher sMatcher = singularPattern.matcher(group);
            Matcher pMatcher = pluralPattern.matcher(group);
            if (sMatcher.find())
            {
                singular.put(mergeString(sMatcher.group(2)), mergeString(sMatcher.group(4)));
            }
            if (pMatcher.find())
            {
                String plurals = pMatcher.group(6);

                Matcher ptMatcher = pluralTPattern.matcher(plurals);
                List<String> pluralT = new ArrayList<String>();
                while (ptMatcher.find())
                {
                    pluralT.add(mergeString(ptMatcher.group(1)));
                }

                plural.put(mergeString(pMatcher.group(4)), pluralT.toArray(new String[0]));

            }
        }
    }

    private static String mergeString(String lines) {
        StringBuilder sb = new StringBuilder();
        for (String line : lines.split("\n"))
        {
            line = line.substring(1, line.length() - 1)
                .replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\")
                .replace("\\\"", "\"");
            sb.append(line);
        }
        return sb.toString();
    }
}
