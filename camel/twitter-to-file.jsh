///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.apache.camel:camel-bom:3.11.1@pom
//DEPS org.apache.camel:camel-core
//DEPS org.apache.camel:camel-main
//DEPS org.apache.camel:camel-stream
//DEPS org.apache.camel:camel-twitter
///DEPS org.slf4j:slf4j-simple:1.7.31
//DEPS org.slf4j:slf4j-nop:1.7.31

/open camel/camel-imports

import static java.lang.System.*;

setProperty("org.slf4j.simpleLogger.logFile", "System.out");
setProperty("camel.main.durationMaxMessages", "1");

var consumerKey = getenv("TWITTER_CONSUMER_KEY");
var consumerSecret = getenv("TWITTER_CONSUMER_SECRET");
var accessToken = getenv("TWITTER_ACCESS_TOKEN");
var accessTokenSecret = getenv("TWITTER_ACCESS_TOKEN_SECRET");

var keyword = "#ApacheCamel";

Main main = new Main();
main.configure().addRoutesBuilder(new RouteBuilder() {
    public void configure() throws Exception {
        from("timer:twitter-to-file?repeatCount=1")
            .toF("twitter-search:%s?consumerKey=%s&consumerSecret=%s&accessToken=%s&accessTokenSecret=%s",
                keyword,
                consumerKey, consumerSecret, accessToken, accessTokenSecret)
            .split(body())
                .transform().simple("${body.text}")
                .to("stream:out")
                .setHeader(Exchange.FILE_NAME, constant("twitter.log"))
                .to("file:out?fileExist=Append&appendChars=\\n");
    }
});
main.run();
