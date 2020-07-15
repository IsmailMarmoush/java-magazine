package io.memoria.magazine.adapter.service;

import io.memoria.jutils.eventsourcing.event.EventHandler;
import io.memoria.magazine.adapter.repo.EventRepo;
import io.memoria.magazine.core.domain.Article;
import io.memoria.magazine.core.services.ArticleService;
import io.memoria.magazine.core.services.dto.ArticleCmd.CreateArticleDraft;
import io.memoria.magazine.core.services.dto.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.core.services.dto.ArticleEvent;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleCreated;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticlePublished;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleTitleEdited;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public record DefaultArticleService(EventRepo<ArticleEvent>repo, EventHandler<Article, ArticleEvent>articleEventHandler)
        implements ArticleService {

  @Override
  public Mono<ArticleCreated> create(String id, CreateArticleDraft createArticle) {
    return repo.exists(id)
               .flatMap(exists -> (exists) ? Mono.error(new AlreadyExists())
                                           : Mono.just(createArticle(id, createArticle)));
  }

  @Override
  public Mono<ArticleTitleEdited> edit(EditArticleTitle editArticleTitle) {
    return repo.stream(editArticleTitle.id())
               .reduce(Article.empty(), articleEventHandler)
               .map(original -> createArticleTitleEdited(editArticleTitle));
  }

  @Override
  public Mono<ArticlePublished> publish(String id) {
    var articleEvents = repo.stream(id);
    var articleMono = articleEvents.reduce(Article.empty(), articleEventHandler);
    return articleMono.map(original -> new ArticlePublished(id, LocalDateTime.now()));
  }

  private ArticleCreated createArticle(String id, CreateArticleDraft createArticle) {
    return new ArticleCreated(id, createArticle.title(), createArticle.content(), LocalDateTime.now());
  }

  private ArticleTitleEdited createArticleTitleEdited(EditArticleTitle editArticleTitle) {
    return new ArticleTitleEdited(editArticleTitle.id(), editArticleTitle.newTitle(), LocalDateTime.now());
  }
}
