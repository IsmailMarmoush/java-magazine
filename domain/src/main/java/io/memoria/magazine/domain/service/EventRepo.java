package io.memoria.magazine.domain.service;

import io.vavr.collection.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventRepo<T> {
  Mono<Void> add(String id, T t);

  Mono<Void> add(String id, List<T> ts);

  Mono<Boolean> exists(String id);

  Flux<T> stream(String id);
}
