package io.memoria.magazine.adapter.repo.memory;

import io.memoria.magazine.adapter.repo.ArticleEventRepo;
import io.memoria.magazine.core.services.dto.ArticleEvent;
import io.vavr.collection.List;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public record InMemoryArticleEventRepo(Map<String, List<ArticleEvent>>db) implements ArticleEventRepo {
  @Override
  public Flux<ArticleEvent> get(String articleId) {
    return Mono.fromCallable(() -> db.get(articleId)).onErrorReturn(List.empty()).flatMapMany(Flux::fromIterable);
  }
}
