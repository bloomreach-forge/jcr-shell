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

import javax.jcr.Node;
import javax.jcr.Session;
import javax.jcr.observation.EventListener;
import java.util.*;

public class JcrShellSession {

    public interface SessionListener {

        void onChangePath();
    }

    SortedMap<String, SortedSet<String>> nodeNameCache = new TreeMap<String, SortedSet<String>>();
    SortedMap<String, SortedSet<String>> propertyNameCache = new TreeMap<String, SortedSet<String>>();
    Object mutex = new Object();
    EventListener cacheListener;

    String server = "http://localhost:8080/";

    String username = "admin";

    char[] password = "admin".toCharArray();

    Session session;

    private Node currentNode;

    private Node previousNode;

    boolean connected;

    List<SessionListener> listeners = new LinkedList<SessionListener>();

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node node) {
        this.previousNode = currentNode;
        this.currentNode = node;
        notifyListeners();
    }

    public boolean cdPrevious() {
        if (previousNode != null) {
            currentNode = previousNode;
            previousNode = null;
            notifyListeners();
            return true;
        }
        return false;
    }

    public void destroy() {
        currentNode = null;
        previousNode = null;
        notifyListeners();
    }

    private void notifyListeners() {
        for (SessionListener listener : listeners) {
            listener.onChangePath();
        }
    }

    public void addListener(SessionListener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(SessionListener listener) {
        this.listeners.remove(listener);
    }
}
