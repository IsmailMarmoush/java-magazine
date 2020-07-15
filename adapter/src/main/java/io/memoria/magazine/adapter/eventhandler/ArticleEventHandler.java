package io.memoria.magazine.adapter.eventhandler;

import io.memoria.jutils.eventsourcing.event.EventHandler;
import io.memoria.magazine.core.domain.Article;
import io.memoria.magazine.core.domain.ArticleStatus;
import io.memoria.magazine.core.services.dto.ArticleEvent;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleCreated;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticlePublished;
import io.memoria.magazine.core.services.dto.ArticleEvent.ArticleTitleEdited;

public record ArticleEventHandler() implements EventHandler<Article, ArticleEvent> {
  @Override
  public Article apply(Article article, ArticleEvent articleEvent) {
    if (articleEvent instanceof ArticleCreated ac) {
      return new Article(ac.title(), ac.content(), ArticleStatus.DRAFT);
    } else if (article.isEmpty()) {
      throw new IllegalStateException("Previous Article is empty");
    } else if (articleEvent instanceof ArticleTitleEdited ac) {
      return changeTitle(article, ac.newTitle());
    } else if (articleEvent instanceof ArticlePublished) {
      return publish(article);
    }
    throw new UnsupportedOperationException("Event is not supported");
  }

  private Article publish(Article article) {
    return switch (article.status()) {
      case DRAFT -> article.toPublished();
      case PUBLISHED -> throw new IllegalStateException("Article already published");
    };
  }

  private Article changeTitle(Article article, String title) {
    return switch (article.status()) {
      case DRAFT -> article.withTitle(title);
      case PUBLISHED -> throw new IllegalStateException("Article already published, can't change the title");
    };
  }
}
