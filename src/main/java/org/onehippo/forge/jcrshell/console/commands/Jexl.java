/*
 *  Copyright 2018 Hippo.
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

import java.io.File;
import java.io.IOException;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlScript;
import org.apache.commons.jexl3.MapContext;
import org.onehippo.forge.jcrshell.console.terminal.FsWrapper;
import org.onehippo.forge.jcrshell.core.JcrShellPrinter;
import org.onehippo.forge.jcrshell.core.JcrWrapper;
import org.onehippo.forge.jcrshell.core.commands.AbstractCommand;

/**
 * Execute a JEXL script file.
 */
public class Jexl extends AbstractCommand {

    public static final String OUT_VAR_NAME = "out";

    public static final String CURRENT_NODE_VAR_NAME = "node";

    private static final ArgumentType[] ARGUMENTS = new ArgumentType[] { ArgumentType.FILE };

    public Jexl() {
        super("jexl", new String[0], "jexl <path>", "execute script in JEXL3 syntax",
                ARGUMENTS);
    }

    @Override
    protected boolean executeCommand(String[] args) throws RepositoryException, IOException {
        final String fileName = FsWrapper.getFullFileName(args[1]);
        final File scriptFile = new File(fileName);

        if (!scriptFile.isFile()) {
            JcrShellPrinter.printWarnln("JEXL script file not found: " + fileName);
            return false;
        }

        try {
            final JexlEngine engine = new JexlBuilder().strict(true).create();
            final JexlScript script = engine.createScript(scriptFile);
            final Object retObj = script.execute(createJexlContext());
            JcrShellPrinter.printOkln("JEXL script executed. Return: " + retObj);
            return true;
        } catch (Exception e) {
            JcrShellPrinter.printErrorln("JEXL script execution error: " + e);
            return false;
        }
    }

    @Override
    protected boolean hasValidArgs(String[] args) {
        return args.length >= 2;
    }

    private JexlContext createJexlContext() {
        JexlContext jexlContext = new MapContext();
        jexlContext.set(OUT_VAR_NAME, JcrShellPrinter.class);
        final Node node = JcrWrapper.getCurrentNode();
        jexlContext.set(CURRENT_NODE_VAR_NAME, node);
        return jexlContext;
    }
}
