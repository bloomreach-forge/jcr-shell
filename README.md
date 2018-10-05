# jcr-shell

Provides shell access to JCR system through JCR over WebDAV protocol.

Documentation site: [https://onehippo-forge.github.io/jcr-shell](https://onehippo-forge.github.io/jcr-shell).

## Prerequisites

You need to open JCR over WebDAV access in your JCR server first.

As an example, you can use the [Hippo Repository JCR over WebDAV Support](https://github.com/onehippo-forge/hippo-jcr-over-webdav) forge module to enable it.

## Download JCR Shell

You can find the latest JCR Shell executable JAR artifact in the following Maven Repository:

- http://maven.onehippo.com/maven2-forge/org/onehippo/forge/jcrshell/

e.g. http://maven.onehippo.com/maven2-forge/org/onehippo/forge/jcrshell/2.0.0/jcrshell-2.0.0.jar

## Run JCR Shell

Execute the JAR like the following:

```bash
$ java -jar jcrshell-2.0.0.jar
```

## (For Developers) Build and run with spring-boot:run

You can build and run it locally with spring-boot Maven plugin:

```bash
$ mvn clean package
$ mvn spring-boot:run
...
```

