package io.memoria.magazine.core.services;

import io.memoria.magazine.core.services.dto.ArticleCmd.CreateArticleDraft;
import io.memoria.magazine.core.services.dto.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleCreated;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticlePublished;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleTitleEdited;
import reactor.core.publisher.Mono;

public interface ArticleService {

  Mono<ArticleCreated> create(String id, CreateArticleDraft createArticle);

  Mono<ArticleTitleEdited> edit(EditArticleTitle editArticleTitle);

  Mono<ArticlePublished> publish(String id);
}
