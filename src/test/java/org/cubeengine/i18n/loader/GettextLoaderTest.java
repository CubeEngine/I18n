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

import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

public class GettextLoaderTest
{

    @Test
    public void testLoading()
    {
        GettextLoader loader = new GettextLoader(Charset.forName("UTF-8"), Collections.<URL>emptyList());
        Map<String, String> singularMessages = new HashMap<String, String>();
        Map<String, String[]> pluralMessages = new HashMap<String, String[]>();
        loader.parseTranslations(poFile, singularMessages, pluralMessages);

        Assert.assertTrue(singularMessages.containsKey("Hello World"));
        Assert.assertEquals(singularMessages.get("Hello World"), "Hallo Welt");

        Assert.assertTrue(singularMessages.containsKey("Long Message\nover multiple lines"));
        Assert.assertEquals(singularMessages.get("Long Message\nover multiple lines"), "Lange Nachricht\nüber mehrere Zeilen");

        Assert.assertTrue(pluralMessages.containsKey("msgid_plural multiline"));
        Assert.assertArrayEquals(pluralMessages.get("msgid_plural multiline"),
                            new String[]{"msgstr 0 multiline", "msgstr 1 multiline"});
    }

    private static String poFile = "msgid \"Hello World\"\n" + "msgstr \"Hallo Welt\"\n" + "\n" + "msgid \"\"\n"
        + "\"msgid multiline\"\n" + "msgid_plural \"\"\n" + "\"msgid_plural multiline\"\n" + "msgstr[0] \"\"\n"
        + "\"msgstr 0 multiline\"\n" + "msgstr[1] \"\"\n" + "\"msgstr 1 multiline\"\n" + "\n" + "msgid \"\"\n"
        + "\"Long Message\\n\"\n" + "\"over multiple lines\"\n" + "msgstr \"\"\n" + "\"Lange Nachricht\\n\"\n"
        + "\"über mehrere Zeilen\"\n";
}