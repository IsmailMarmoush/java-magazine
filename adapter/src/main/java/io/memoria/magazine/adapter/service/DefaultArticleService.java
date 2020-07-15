package io.memoria.magazine.adapter.service;

import io.memoria.magazine.adapter.eventhandler.ArticleEventHandler;
import io.memoria.magazine.adapter.repo.ArticleEventRepo;
import io.memoria.magazine.core.domain.Article;
import io.memoria.magazine.core.services.ArticleService;
import io.memoria.magazine.core.services.dto.ArticleCmd.CreateArticleDraft;
import io.memoria.magazine.core.services.dto.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleCreated;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticlePublished;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleTitleEdited;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static io.memoria.magazine.core.domain.ArticleStatus.PUBLISHED;

public record DefaultArticleService(ArticleEventRepo articleEventRepo, ArticleEventHandler articleEventHandler)
        implements ArticleService {

  @Override
  public Mono<ArticleCreated> create(String id, CreateArticleDraft createArticle) {
    // TODO Validations etc here
    return Mono.just(new ArticleCreated(id, createArticle.title(), createArticle.content(), LocalDateTime.now()));
  }

  @Override
  public Mono<ArticleTitleEdited> edit(EditArticleTitle editArticleTitle) {
    return articleEventRepo.get(editArticleTitle.id()).reduce(Article.empty(), articleEventHandler).map(original -> {
      if (original.status().equals(PUBLISHED)) {
        throw new UnsupportedOperationException("Article was already published");
      } else {
        return new ArticleTitleEdited(editArticleTitle.id(), editArticleTitle.newTitle(), LocalDateTime.now());
      }
    });
  }

  @Override
  public Mono<ArticlePublished> publish(String id) {
    var articleEvents = articleEventRepo.get(id);
    var articleMono = articleEvents.reduce(Article.empty(), articleEventHandler);
    return articleMono.map(original -> new ArticlePublished(id, LocalDateTime.now()));
  }
}
