package io.memoria.magazine.core.services;

import io.memoria.magazine.core.services.dto.ArticleCmd.CreateArticleDraft;
import io.memoria.magazine.core.services.dto.ArticleCmd.EditArticle;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleCreated;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleTitleEdited;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticlePublished;
import reactor.core.publisher.Mono;

public interface ArticleService {

  Mono<ArticleCreated> create(CreateArticleDraft createArticle);

  Mono<ArticleTitleEdited> edit(EditArticle editArticle);

  Mono<ArticlePublished> publish(String id);
}
