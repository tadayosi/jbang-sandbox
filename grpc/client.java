///usr/bin/env jbang "$0" "$@" ; exit $?

//SOURCES hello/Hello.java
//SOURCES hello/HelloServiceGrpc.java

//DEPS com.google.protobuf:protobuf-java:4.28.3
//DEPS com.google.protobuf:protobuf-java-util:4.28.3
//DEPS io.grpc:grpc-netty-shaded:1.68.1
//DEPS io.grpc:grpc-protobuf:1.68.1
//DEPS io.grpc:grpc-stub:1.68.1
//DEPS org.apache.tomcat:annotations-api:6.0.53

import static java.lang.System.out;

import hello.Hello.HelloRequest;
import hello.HelloServiceGrpc;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;

public class client {

    static String target = "localhost:50051";

    public static void main(String... args) {
        var channel = Grpc.newChannelBuilder(target, InsecureChannelCredentials.create())
                .build();
        var stub = HelloServiceGrpc.newBlockingStub(channel);
        var request = HelloRequest.newBuilder()
                .setName("JBang")
                .build();
        var response = stub.hello(request);
        out.println("Response: " + response.getMessage());
    }
}
