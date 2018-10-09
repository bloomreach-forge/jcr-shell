/*
 *  Copyright 2008-2018 Hippo.
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
package org.onehippo.forge.jcrshell.console.commands;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

import org.onehippo.forge.jcrshell.core.JcrShellPrinter;
import org.onehippo.forge.jcrshell.core.commands.AbstractCommand;

/**
 * Get system property.
 */
public class SysPropGet extends AbstractCommand {

    private static final ArgumentType[] ARGUMENTS = new ArgumentType[] { ArgumentType.STRING };

    public SysPropGet() {
        super("syspropget", new String[] { "sysprop", "getsysprop" }, "syspropget [<property>]",
                "get the value of system property", ARGUMENTS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected final boolean executeCommand(final String[] args) {
        if (args.length == 1) {
            final Enumeration<?> e = System.getProperties().propertyNames();
            final StringWriter sw = new StringWriter(1024);
            final PrintWriter out = new PrintWriter(sw);
            while (e.hasMoreElements()) {
                final String key = (String) e.nextElement();
                final String value = System.getProperty(key);
                out.println(key + " = " + value);
            }
            out.flush();
            JcrShellPrinter.printOkln(sw.toString());
        } else {
            final String value = System.getProperty(args[1]);

            if (value != null) {
                JcrShellPrinter.printOkln(args[1] + " = " + value);
            } else {
                JcrShellPrinter.printWarnln("System property not found by key, '" + args[1] + "'.");
            }
        }

        return true;
    }

    @Override
    protected boolean hasValidArgs(String[] args) {
        return args.length >= 1;
    }

    @Override
    protected boolean needsLiveSession() {
        return false;
    }
}
