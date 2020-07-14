package io.memoria.magazine.adapter.article;

import io.memoria.magazine.core.domain.Article;
import io.memoria.magazine.core.domain.ArticleStatus;
import io.memoria.magazine.core.services.ArticleService;
import io.memoria.magazine.core.services.dto.ArticleCmd.CreateArticleDraft;
import io.memoria.magazine.core.services.dto.ArticleCmd.EditArticle;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleCreated;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleTitleEdited;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticlePublished;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.memoria.magazine.core.domain.ArticleStatus.PUBLISHED;

public record DefaultArticleService(ArticleEventRepo articleEventRepo, ArticleEventHandler articleEventHandler)
        implements ArticleService {

  @Override
  public Mono<ArticleCreated> create(CreateArticleDraft createArticle) {
    // Validations etc here
    return Mono.just(new ArticleCreated(UUID.randomUUID().toString(),
                                        createArticle.title(),
                                        createArticle.content(),
                                        LocalDateTime.now()));
  }

  @Override
  public Mono<ArticleTitleEdited> edit(EditArticle editArticle) {
    var articleEvents = articleEventRepo.get(editArticle.id());
    var articleMono = articleEvents.reduce(Article.empty(), articleEventHandler);
    return articleMono.map(original -> {
      if (original.status().equals(PUBLISHED)) {
        throw new UnsupportedOperationException("Article was already published");
      } else {
        return new ArticleTitleEdited(editArticle.id(), editArticle.newTitle(), LocalDateTime.now());
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
