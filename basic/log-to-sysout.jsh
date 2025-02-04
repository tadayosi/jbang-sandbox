///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.slf4j:slf4j-simple:1.7.31

import static java.lang.System.setProperty;
import static java.util.stream.IntStream.*;
import static org.slf4j.LoggerFactory.*;

setProperty("org.slf4j.simpleLogger.logFile", "System.out");

var log = getLogger("log-to-sysout");

range(0, 10).forEach(i -> {
    log.info("Hello! #{}", i);
});
