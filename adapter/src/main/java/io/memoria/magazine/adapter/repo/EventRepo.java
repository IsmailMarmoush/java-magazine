package io.memoria.magazine.adapter.repo;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventRepo<T> {
  Mono<Void> add(String id, T t);

  Mono<Boolean> exists(String id);

  Flux<T> stream(String id);
}
