///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.slf4j:slf4j-simple:1.7.31

import java.util.stream.*;
import org.slf4j.*;

System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");

Logger log = LoggerFactory.getLogger("log-to-sysout");

IntStream.range(0, 10).forEach(i -> {
    log.info("Hello! #{}", i);
});
