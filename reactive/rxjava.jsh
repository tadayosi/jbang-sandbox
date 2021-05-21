///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.reactivestreams:reactive-streams:1.0.3
//DEPS io.reactivex.rxjava3:rxjava:3.0.12

/open PRINTING

import org.reactivestreams.*
import io.reactivex.rxjava3.core.*

// Hello world
Flowable.just("Hello World").subscribe(System.out::println)

// Publisher/Subscriber example
Publisher<Integer> publisher = Flowable.just(1, 2, 3, 4, 5)
Flowable.fromPublisher(publisher).
    map(i -> i*i).
    doOnNext(System.out::println).
    subscribe()
