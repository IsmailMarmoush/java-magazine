package io.memoria.magazine.domain.services;

import io.memoria.magazine.domain.model.article.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.domain.model.article.ArticleCmd.SubmitDraft;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleCreated;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticlePublished;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;
import reactor.core.publisher.Mono;

public interface ArticleService {

  Mono<ArticleCreated> create(String id, SubmitDraft submitDraft);

  Mono<ArticleTitleEdited> edit(EditArticleTitle editArticleTitle);

  Mono<ArticlePublished> publish(String id);
}
