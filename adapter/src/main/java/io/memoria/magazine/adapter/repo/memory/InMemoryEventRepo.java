package io.memoria.magazine.adapter.repo.memory;

import io.memoria.magazine.adapter.repo.EventRepo;
import io.vavr.collection.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public class InMemoryEventRepo<T> implements EventRepo<T> {
  public final Map<String, List<T>> db;

  public InMemoryEventRepo(Map<String, List<T>> db) {
    this.db = db;
  }

  @Override
  public Mono<Void> add(String id, T t) {
    return Mono.fromRunnable(() -> {
      if (db.get(id) == null || db.get(id).isEmpty()) {
        db.put(id, List.of(t));
      } else {
        db.put(id, db.get(id).append(t));
      }
    });
  }

  @Override
  public Mono<Boolean> exists(String id) {
    return Mono.fromCallable(() -> db.containsKey(id));
  }

  @Override
  public Flux<T> stream(String id) {
    return Mono.fromCallable(() -> db.get(id)).onErrorReturn(List.empty()).flatMapMany(Flux::fromIterable);
  }
}
