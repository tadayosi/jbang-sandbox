///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.squareup.okhttp3:mockwebserver:3.14.7

/open PRINTING

import java.security.*
import javax.net.ssl.*

import okhttp3.mockwebserver.*

println("Starting...")

MockWebServer server;
try (FileInputStream is = new FileInputStream(new File("certs/server-keystore.jks"))) {
    KeyStore keyStore = KeyStore.getInstance("JKS");
    keyStore.load(is, "secret".toCharArray());
    KeyManagerFactory kmFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmFactory.init(keyStore, "secret".toCharArray());
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmFactory.getKeyManagers(), null, null);
    server = new MockWebServer();
    server.useHttps(sslContext.getSocketFactory(), false);
}

server.enqueue(new MockResponse().setBody("Hello!\n"))

server.start(9999)

boolean bye = false
while (!bye) {
    String msg = server.takeRequest().getBody().readUtf8().trim();
    println(msg);
    switch (msg) {
        case "bye":
            bye = true;
            break;
        default:
            server.enqueue(new MockResponse().setBody("Hello!\n"));
    }
}

server.shutdown()
