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
package org.onehippo.forge.jcrshell.core.commands;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;

import org.onehippo.forge.jcrshell.core.JcrShellPrinter;
import org.onehippo.forge.jcrshell.core.JcrWrapper;

/**
 * List child nodes of current node.
 */
public class MixinList extends AbstractCommand {

    public MixinList() {
        super("mixinlist", new String[] { "listmixins" }, "mixinlist", "list mixins of current node");
    }

    /**
     * {@inheritDoc}
     * @throws RepositoryException 
     */
    @Override
    protected final boolean executeCommand(final String[] args) throws RepositoryException {
        Node node = JcrWrapper.getCurrentNode();
        if (!node.hasProperty("jcr:mixinTypes")) {
            JcrShellPrinter.println("Node has no mixins.");
            return true;
        }

        for (Value val : node.getProperty("jcr:mixinTypes").getValues()) {
            JcrShellPrinter.println(val.getString());
        }
        return true;
    }

    @Override
    protected boolean hasValidArgs(String[] args) {
        return args.length == 1;
    }
}
