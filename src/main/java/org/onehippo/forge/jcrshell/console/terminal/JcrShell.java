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
package org.onehippo.forge.jcrshell.console.terminal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.jcr.RepositoryException;

import org.onehippo.forge.jcrshell.console.completers.DirNameCompleter;
import org.onehippo.forge.jcrshell.console.completers.FileNameCompleter;
import org.onehippo.forge.jcrshell.core.Command;
import org.onehippo.forge.jcrshell.core.CommandHelper;
import org.onehippo.forge.jcrshell.core.JcrShellSession;
import org.onehippo.forge.jcrshell.core.JcrWrapper;
import org.onehippo.forge.jcrshell.core.completers.CompleterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main wrapper to start the shell.
 */
public final class JcrShell {

    private static final String DEFAULT_COMMANDS = "/META-INF/org.onehippo.forge.jcrshell.commands.core.properties";
    private static final String CONSOLE_COMMANDS = "/META-INF/org.onehippo.forge.jcrshell.commands.console.properties";
    private static final String EXTRA_COMMANDS = "/META-INF/org.onehippo.forge.jcrshell.commands.extra.properties";

    public static final String NOT_CONNECTED_PROMPT = "jcr-shell:>";
    private static final int MAX_PROMPT_LENGTH = 15;

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(JcrShell.class);

    /**
     * Hide constructor
     */
    private JcrShell() {
    }

    /**
     * The main method to start the jcr shell.
     * @param args ignored
     * @throws IOException io failure when starting shell
     */
    public static void main(final String[] args) throws IOException {
        final Terminal term = setupTerminal();
        registerCompleters();
        registerCommands();
        registerShutdownHook();
        if (args.length == 0) {
            final JcrShellSession session = new JcrShellSession();
            JcrWrapper.setShellSession(session);
            session.addListener(new JcrShellSession.SessionListener() {

                @Override
                public void onChangePath() {
                    if (!JcrWrapper.isConnected() || session.getCurrentNode() == null) {
                        term.setCommandLinePrompt(NOT_CONNECTED_PROMPT);
                        return;
                    }
                    try {
                        String path = session.getCurrentNode().getPath();
                        if (path.length() > MAX_PROMPT_LENGTH) {
                            String dots = "...";
                            path = dots + path.substring(path.length() - MAX_PROMPT_LENGTH + dots.length());
                        }
                        term.setCommandLinePrompt(JcrWrapper.getUsername() + ":" + path + ">");
                    } catch (RepositoryException e) {
                        log.info("Unable to determine path while setting prompt", e);
                        term.setCommandLinePrompt(JcrWrapper.getUsername() + ":[unknown]>");
                    }
                }
            });

            runShell(term);
        } else {
            runScript(args);
        }
    }

    public static Terminal setupTerminal() {
        Terminal term = new Terminal();
        term.setCommandLinePrompt(NOT_CONNECTED_PROMPT);
        StringBuilder version = new StringBuilder();
        // TODO: read from somethings automatically.
        version.append("JCR Shell 2.0.0");
        version.append(System.getProperty("line.separator"));
        term.setCommandLineVersion(version.toString());
        return term;
    }

    public static void registerCompleters() {
        CompleterFactory.registerCompleter(Command.ArgumentType.Flags.FILE, FileNameCompleter.class);
        CompleterFactory.registerCompleter(Command.ArgumentType.Flags.DIRECTORY, DirNameCompleter.class);
    }

    public static void registerCommands() {
        CommandHelper.loadCommandsFromResource(DEFAULT_COMMANDS);
        CommandHelper.loadCommandsFromResource(CONSOLE_COMMANDS);
        CommandHelper.loadCommandsFromResource(EXTRA_COMMANDS);
    }

    public static void registerShutdownHook() {
        Terminal.ShutdownHook sh = Terminal.getShutdownHook();
        Runtime.getRuntime().addShutdownHook(sh);
    }

    public static void runShell(Terminal term) {
        term.start();
    }

    public static void runScript(final String[] args) {
        try {
            Terminal.run(new FileInputStream(new File(args[0])), System.out);
        } catch (FileNotFoundException e) {
            log.error("Script file not found '{}'", args[0]);
        }
    }

}
