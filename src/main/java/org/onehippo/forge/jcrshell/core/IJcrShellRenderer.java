package org.onehippo.forge.jcrshell.core;

import org.onehippo.forge.jcrshell.core.output.Output;

import java.util.List;

public interface IJcrShellRenderer {

    void print(Output output);

    void printTableWithHeader(List<String[]> rows);
}
