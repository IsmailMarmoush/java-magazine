package io.memoria.magazine.adapter.repo;

import io.memoria.magazine.core.services.dto.ArticleEvent;
import reactor.core.publisher.Flux;

public interface ArticleEventRepo {
  Flux<ArticleEvent> get(String articleId);
}
