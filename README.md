# jcr-shell

Provides shell access to JCR system through JCR over WebDAV protocol.

## Prerequisites

You need to open JCR over WebDAV access in your JCR server first.

As an example, you can use the [Hippo Repository JCR over WebDAV Support](https://github.com/onehippo-forge/hippo-jcr-over-webdav) forge module to enable it.

## Download JCR Shell

You can find the latest JCR Shell executable JAR artifact in the following Maven Repository:

- http://maven.onehippo.com/maven2-forge/org/onehippo/forge/jcr-shell/

e.g. http://maven.onehippo.com/maven2-forge/org/onehippo/forge/jcr-shell/2.0.0/jcr-shell-2.0.0.jar

## Run JCR Shell

Execute the JAR like the following:

```bash
$ java -jar jcr-shell-2.0.0.jar
```

### Example interaction with various commands

```
$ java -jar jcrshell-2.0.0.jar

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v2.0.4.RELEASE)

2018-09-28 17:23:37.790 ERROR 4246 --- [           main] o.o.forge.jcrshell.core.CommandHelper    : Class not found 'org.onehippo.forge.jcrshell.console.commands.Repository': org.onehippo.forge.jcrshell.console.commands.Repository
JCR Shell 2.0.0

exit or quit leaves program.
help lists commands.

jcr-shell:> help
----------------------------------------------------------------------------------------------------------------
Command             Usage                                                                           
----------------------------------------------------------------------------------------------------------------
aliases             aliases                                                                         
cd                  cd [<path>|<reference property>]                                                
cdprev              cdprev                                                                          
...
----------------------------------------------------------------------------------------------------------------
Command completed in 11 msecs.

jcr-shell:> server http://localhost:8080/cms/server
Command completed in 0 msecs.

jcr-shell:> credentials admin
password: *****
Command completed in 1187 msecs.

jcr-shell:> login

done.
Command completed in 739 msecs.

admin:/> cd content/documents/hipporepojcrdavdemo/news/2015/11/the-medusa-news/the-medusa-news/
Command completed in 1 msecs.

admin:...-medusa-news> ls
----------------------------------------------------------------------------------------------------------------
Name                         Type                          
----------------------------------------------------------------------------------------------------------------
hipporepojcrdavdemo:content  hippostd:html                 
hipporepojcrdavdemo:image    hippogallerypicker:imagelink  
----------------------------------------------------------------------------------------------------------------
Command completed in 30 msecs.

admin:...-medusa-news> propget hipporepojcrdavdemo:title 
The medusa news
Command completed in 5 msecs.

admin:...-medusa-news> logout
Command completed in 2176 msecs.

jcr-shell:> quit
Bye bye!

```

## (For Developers) Build and run with spring-boot:run

You can build and run it locally with spring-boot Maven plugin:

```bash
$ mvn clean package
$ mvn spring-boot:run
...
```

