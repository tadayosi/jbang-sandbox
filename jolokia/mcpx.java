///usr/bin/env jbang --javaagent=org.jolokia.mcp:jolokia-mcp-agent-jvm:0.4.1:javaagent "$0" "$@" ; exit $?

//JAVA 17
//DEPS com.fasterxml.jackson:jackson-bom:2.18.3@pom
//DEPS com.fasterxml.jackson.core:jackson-core
//DEPS com.fasterxml.jackson.core:jackson-databind
//DEPS io.vertx:vertx-stack-depchain:4.5.16@pom
//DEPS io.vertx:vertx-core
//DEPS io.netty:netty-resolver-dns-native-macos:4.2.3.Final:osx-aarch_64
//DEPS org.slf4j:slf4j-simple:2.0.17

import static java.lang.System.err;
import static java.lang.System.out;
import static java.lang.System.setProperty;

import java.lang.management.ManagementFactory;
import java.math.BigInteger;

import javax.management.ObjectName;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

public class mcpx {

    static final String objectName = "com.github.tadayosi.samples.jbang:name=Sample";
    static final Sample sampleMBean = new Sample();

    public static void main(String[] args) {
        setProperty("org.slf4j.simpleLogger.defaultLogLevel", "INFO");

        onStart();

        var vertx = Vertx.vertx();
        vertx.deployVerticle(new LoggingVerticle());

        var eventBus = vertx.eventBus();
        vertx.createHttpServer()
                .requestHandler(req -> {
                    eventBus.send("logging", "Got request: " + req.method() + " " + req.uri());
                    req.response().end("Hello!");
                })
                .listen(8888)
                .onSuccess(s -> out.println("HTTP server started on port: " + s.actualPort()));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            onStop();
            vertx.close();
        }));
    }

    static class LoggingVerticle extends AbstractVerticle {
        public void start() {
            out.println("[Logging] " + getClass().getSimpleName() + " running");
            vertx.eventBus().consumer("logging", m -> out.println("[Logging] " + m.body()));
        }
    }

    static void onStart() {
        out.println("Register a mbean: " + objectName);
        try {
            var mbeanServer = ManagementFactory.getPlatformMBeanServer();
            mbeanServer.registerMBean(sampleMBean, new ObjectName(objectName));
        } catch (Exception e) {
            err.println(e.getMessage());
        }
    }

    static void onStop() {
        out.println("Deregister a mbean: " + objectName);
        try {
            var mbeanServer = ManagementFactory.getPlatformMBeanServer();
            mbeanServer.unregisterMBean(new ObjectName(objectName));
        } catch (Exception e) {
            err.println(e.getMessage());
        }
    }

    public interface SampleMBean {
        String hello(String message);

        String html();

        long longValue(long value);

        BigInteger bigIntegerValue(BigInteger value);
    }

    public static class Sample implements SampleMBean {
        public String hello(String message) {
            out.println("hello: " + message);
            return "Hello World";
        }

        public String html() {
            out.println("html");
            return """
                    <!DOCTYPE html>
                    <html>
                    <body>
                    <table class="pf-v5-c-table">
                      <thead class="pf-v5-c-table__thead">
                        <tr class="pf-v5-c-table__tr">
                          <th class="pf-v5-c-table__th">key1</td>
                          <th class="pf-v5-c-table__th">key2</td>
                        </tr>
                      </thead>
                      <tbody class="pf-v5-c-table__tbody">
                        <tr class="pf-v5-c-table__tr">
                          <td class="pf-v5-c-table__td">value1</td>
                          <td class="pf-v5-c-table__td">value2</td>
                        </tr>
                        <tr class="pf-v5-c-table__tr">
                          <td class="pf-v5-c-table__td">value3</td>
                          <td class="pf-v5-c-table__td">value4</td>
                        </tr>
                      </tbody>
                    </table>
                    </body>
                    </html>
                    """;
        }

        public long longValue(long value) {
            out.println("long: " + value);
            return Long.MAX_VALUE;
        }

        public BigInteger bigIntegerValue(BigInteger value) {
            out.println("BigInteger: " + value);
            return BigInteger.valueOf(Long.MAX_VALUE).pow(2);
        }
    }
}
