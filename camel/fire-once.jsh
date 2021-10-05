///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.11.1@pom
//DEPS org.apache.camel:camel-core-engine
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-timer
//DEPS org.apache.camel:camel-stream
//DEPS org.slf4j:slf4j-simple:1.7.31

/open camel/camel-imports

import static java.lang.System.*;

setProperty("org.slf4j.simpleLogger.logFile", "System.out");
setProperty("camel.main.durationMaxMessages", "1");

Main main = new Main();
main.configure().addRoutesBuilder(new RouteBuilder() {
    public void configure() throws Exception {
        from("timer:fire-once?repeatCount=1")
            .setBody().constant("Hello Camel!")
            .to("stream:out");
    }
});
main.run();
