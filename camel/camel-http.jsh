///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.8.0@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-http
//DEPS org.slf4j:slf4j-nop:1.7.30

/open camel/camel-imports

import org.apache.camel.support.jsse.*
import org.apache.camel.component.http.*

Main main = new Main();
main.configure().addRoutesBuilder(new RouteBuilder() {
    @Override
    public void configure() throws Exception {
        KeyStoreParameters ksp = new KeyStoreParameters();
        ksp.setResource("certs/client-truststore.jks");
        ksp.setPassword("secret");
        TrustManagersParameters tmp = new TrustManagersParameters();
        tmp.setKeyStore(ksp);
        SSLContextParameters scp = new SSLContextParameters();
        scp.setTrustManagers(tmp);

        //getContext().getComponent("https", HttpComponent.class)
        //    .setSslContextParameters(scp);
        getContext().getRegistry().bind("scp", scp);

        from("timer:hello?period=3000")
            .setBody().constant("What's up!\n")
            .to("stream:out")
            .to("https://localhost:9999/hello?sslContextParameters=#scp")
            .to("stream:out");
    }
});
main.run();
