/*
 *  Copyright 2009 Hippo.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.onehippo.forge.jcrshell.core.export;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

public final class XmlFormatter {

    // private constructor for utility class
    private XmlFormatter() {
    }

    public static void format(File in, File out) throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        InputStream istream = new FileInputStream(in);
        OutputStream ostream = null;
        OutputProcessor processor;
        try {
            try {
                if (!out.exists()) {
                    ostream = new FileOutputStream(out);
                    processor = new OutputProcessor(factory, istream, ostream);
                } else {
                    XmlProcessor recordProcessor = new XmlProcessor(factory, out);
                    recordProcessor.process();
                    ostream = new FileOutputStream(out);
                    processor = new OutputProcessor(factory, recordProcessor, istream, ostream);
                }
                processor.process();
            } finally {
                if (ostream != null) {
                    ostream.close();
                }
            }
        } finally {
            istream.close();
        }
    }
    
}
