///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS com.squareup.okhttp3:mockwebserver:3.14.7
//DEPS com.squareup.okhttp3:okhttp-tls:3.14.7

/open PRINTING

import java.security.*
import javax.net.ssl.*

import okhttp3.*
import okhttp3.mockwebserver.*
import okhttp3.tls.*

println("Starting...")

String hostname = InetAddress.getByName("localhost").getCanonicalHostName()
HeldCertificate cert = new HeldCertificate.Builder().addSubjectAlternativeName(hostname).build()
HandshakeCertificates certs = new HandshakeCertificates.Builder().heldCertificate(cert).build()
MockWebServer server = new MockWebServer()
server.useHttps(certs.sslSocketFactory(), false)

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
