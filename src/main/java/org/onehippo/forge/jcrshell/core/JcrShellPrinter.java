/*
 *  Copyright 2010-2018 Hippo.
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
package org.onehippo.forge.jcrshell.core;

import java.util.List;

import org.onehippo.forge.jcrshell.core.output.Output;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class JcrShellPrinter {

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(JcrShellPrinter.class);

    private static ThreadLocal<IJcrShellRenderer> tlPrinter = new ThreadLocal<IJcrShellRenderer>();

    public static void setConsolePrinter(IJcrShellRenderer printer) {
        JcrShellPrinter.tlPrinter.set(printer);
    }

    /**
     * Private constructor for utility class
     */
    private JcrShellPrinter() {
    }

    //------------------- public print methods ----------------------------//

    public static void print(Output output) {
        if (tlPrinter.get() == null) {
            log.warn("No printer attached to the current shell.");
            return;
        }

        tlPrinter.get().print(output.head());
    }

    public static void println(final CharSequence s) {
        print(Output.out().a(s.toString()));
    }

    public static void printDebugln(final CharSequence s) {
        print(Output.out().debug(s.toString()));
    }

    public static void printOkln(final CharSequence s) {
        print(Output.out().ok(s.toString()));
    }

    public static void printWarnln(final CharSequence s) {
        print(Output.out().warn(s.toString()));
    }

    public static void printErrorln(final CharSequence s) {
        print(Output.out().error(s.toString()));
    }

    //------------------- table print helpers ----------------------------//
    public static void printTableWithHeader(List<String[]> rows) {
        if (tlPrinter.get() == null) {
            log.warn("No printer attached to the current shell.");
            return;
        }

        tlPrinter.get().printTableWithHeader(rows);
    }
}