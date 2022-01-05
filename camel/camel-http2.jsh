///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.14.0@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-http
//DEPS org.slf4j:slf4j-simple:1.7.31

System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");

/open camel/camel-imports

import org.apache.camel.support.jsse.*;
import org.apache.camel.component.http.*;

Main main = new Main();

Map<String, Object> props = Map.of(
    "camel.beans.ksp", "#class:org.apache.camel.support.jsse.KeyStoreParameters",
    "camel.beans.ksp.resource", "certs/client-truststore.jks",
    "camel.beans.ksp.password", "secret",
    "camel.beans.tmp", "#class:org.apache.camel.support.jsse.TrustManagersParameters",
    "camel.beans.tmp.keyStore", "#bean:ksp",
    "camel.beans.scp", "#class:org.apache.camel.support.jsse.SSLContextParameters",
    "camel.beans.scp.trustManagers", "#bean:tmp");

main.setInitialProperties(props);

main.configure().addRoutesBuilder(new RouteBuilder() {
    public void configure() throws Exception {
        from("timer:hello?period=3000")
            .setBody().constant("What's up!\n")
            .to("stream:out")
            .to("https://localhost:9999/hello?sslContextParameters=#scp")
            .to("stream:out");
    }
});
main.run();
