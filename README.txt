===============================
How to test and build jcr-shell
===============================

1. Introduction

  Very brief introduction to how to build, test and run jcr-shell!

2. Requirements --

  (1) Java 1.6
  (2) Maven 3.0.x

3. Build

  (1) Build with testing

    $ mvn clean install

  (2) Build with skipping tests

    $ mvn clean install -DskipTests

4. How to run the console during development

  (1) Move to `console' folder and run the following:

    $ mvn exec:java

5. How to tag

  (1) In the project root folder, run the following:

    $ mvn release:perform -DscmCommentPrefix="HIPPLUG-XXX: "

    (Change the JIRA issue number properly.)

6. Create distribution files

  (1) In a temporary folder, check out the newly tagged source and build it like the following example:

    $ cd /tmp
    $ svn checkout https://forge.onehippo.org/svn/jcr-shell/tags/jcrshell-1.01.03 jcrshell-1.01.03
    $ cd jcrshell-1.01.03
    $ mvn clean install

  (2) Install `console' artifacts and assemble the console application:
      Move to `console' folder and run the following:

    $ cd console
    $ mvn appassembler:assemble
    $ mvn install:install-file -Dfile=target/jcrshell-console-1.01.03.jar -DpomFile=pom.xml -DlocalRepositoryPath=target/jcr-shell/repo

  (2) Move to `console/target' folder and compress the the assembled application directory:

    $ cd target
    $ tar cvfz jcr-shell.tgz jcr-shell/
    $ zip -r jcr-shell.zip jcr-shell/
