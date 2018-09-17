/*
 *  Copyright 2008 Hippo.
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

import java.io.IOException;

import javax.jcr.LoginException;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.ConfigurationException;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class JcrTest {

    static TransientRepository repository;

    private static Session session;
    private Node testRoot;

    @BeforeClass
    public static void startRepository() throws IOException, ConfigurationException, RepositoryException {
        RepositoryConfig config = RepositoryConfig.create(
                JcrTest.class.getClassLoader().getResourceAsStream(
                "repository.xml"), "target/repository");
        repository = new TransientRepository(config);
        session = repository.login();
    }

    @AfterClass
    public static void shutdownRepository() {
        session.logout();
    }

    @Before
    public void startSession() throws LoginException, RepositoryException {
        testRoot = session.getRootNode().addNode("test");
        JcrWrapper.setShellSession(new JcrShellSession());
        JcrWrapper.setUsername("test-user");
        JcrWrapper.setCurrentNode(testRoot);
        JcrWrapper.setConnected(true);
    }

    @After
    public void stopSession() throws RepositoryException {
        JcrWrapper.setConnected(false);
        JcrWrapper.setShellSession(null);
        testRoot.remove();
    }

    protected final Session getSession() {
        return session;
    }

    protected final Node getTestRoot() {
        return testRoot;
    }

}
