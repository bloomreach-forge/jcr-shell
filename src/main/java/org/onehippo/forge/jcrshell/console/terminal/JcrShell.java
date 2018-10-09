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

import org.onehippo.forge.jcrshell.boot.ShellConfiguration;
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
public class JcrShell {

    private static final String DEFAULT_COMMANDS = "/META-INF/org.onehippo.forge.jcrshell.commands.core.properties";
    private static final String CONSOLE_COMMANDS = "/META-INF/org.onehippo.forge.jcrshell.commands.console.properties";
    private static final String EXTRA_COMMANDS = "/META-INF/org.onehippo.forge.jcrshell.commands.extra.properties";

    private static final String NOT_CONNECTED_PROMPT = "jcr-shell:> ";
    private static final int MAX_PROMPT_LENGTH = 15;

    /** logger */
    private static final Logger log = LoggerFactory.getLogger(JcrShell.class);

    public JcrShell() {
    }

    /**
     * The main method to start the jcr shell.
     * @throws IOException io failure when starting shell
     */
    public void startShell(final ShellConfiguration shellConfiguration) throws IOException {
        final Terminal term = setupTerminal();

        registerCompleters();
        registerCommands();
        registerShutdownHook();

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
                    term.setCommandLinePrompt(JcrWrapper.getUsername() + ":" + path + "> ");
                } catch (RepositoryException e) {
                    log.info("Unable to determine path while setting prompt", e);
                    term.setCommandLinePrompt(JcrWrapper.getUsername() + ":[unknown]> ");
                }
            }
        });

        setUpServerAndUser(shellConfiguration);

        runShell(term);
    }

    public void executeBatch(final ShellConfiguration shellConfiguration) throws IOException {
        registerCommands();
        registerShutdownHook();

        final JcrShellSession session = new JcrShellSession();
        JcrWrapper.setShellSession(session);

        setUpServerAndUser(shellConfiguration);

        runScript(shellConfiguration.getBatch().trim());
    }

    private Terminal setupTerminal() {
        Terminal term = new Terminal();
        term.setCommandLinePrompt(NOT_CONNECTED_PROMPT);
        StringBuilder version = new StringBuilder();
        // TODO: read from somethings automatically.
        version.append("JCR Shell 2.0.0");
        version.append(System.getProperty("line.separator"));
        term.setCommandLineVersion(version.toString());
        return term;
    }

    private void registerCompleters() {
        CompleterFactory.registerCompleter(Command.ArgumentType.Flags.FILE, FileNameCompleter.class);
        CompleterFactory.registerCompleter(Command.ArgumentType.Flags.DIRECTORY, DirNameCompleter.class);
    }

    private void registerCommands() {
        CommandHelper.loadCommandsFromResource(DEFAULT_COMMANDS);
        CommandHelper.loadCommandsFromResource(CONSOLE_COMMANDS);
        CommandHelper.loadCommandsFromResource(EXTRA_COMMANDS);
    }

    private void registerShutdownHook() {
        Terminal.ShutdownHook sh = Terminal.getShutdownHook();
        Runtime.getRuntime().addShutdownHook(sh);
    }

    private void runShell(Terminal term) {
        term.start();
    }

    private void runScript(final String script) {
        try {
            Terminal.run(new FileInputStream(new File(script)), System.out);
        } catch (FileNotFoundException e) {
            log.error("Script file not found '{}'", script);
        }
    }

    private void setUpServerAndUser(final ShellConfiguration shellConfiguration) {
        String server = shellConfiguration.getServer();

        if (server != null) {
            server = server.trim();

            if (!server.isEmpty()) {
                JcrWrapper.setServer(server);
            }
        }

        String user = shellConfiguration.getUser();

        if (user != null) {
            user = user.trim();

            if (!user.isEmpty()) {
                final int offset = user.indexOf(':');
                final String username = (offset != -1) ? user.substring(0, offset) : user;
                final String password = (offset != -1) ? user.substring(offset + 1) : null;
                JcrWrapper.setUsername(username);

                if (password != null) {
                    JcrWrapper.setPassword(password);
                }
            }
        }
    }
}
