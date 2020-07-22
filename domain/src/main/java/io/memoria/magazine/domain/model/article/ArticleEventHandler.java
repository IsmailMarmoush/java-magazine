package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.event.EventHandler;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticlePublished;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;
import io.memoria.magazine.domain.model.article.ArticleEvent.DraftArticleSubmitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record ArticleEventHandler() implements EventHandler<Article, ArticleEvent> {
  private static final Logger log = LoggerFactory.getLogger(ArticleEventHandler.class.getName());

  @Override
  public Article apply(Article article, ArticleEvent articleEvent) {
    if (articleEvent instanceof DraftArticleSubmitted ac) {
      return new Article(ac.articleId(), ac.creatorId(), ac.title(), ac.content(), ArticleStatus.DRAFT, ac.topics());
    } else if (articleEvent instanceof ArticleTitleEdited ac) {
      return article.withTitle(ac.newTitle());
    } else if (articleEvent instanceof ArticlePublished) {
      return article.toPublished();
    } else {
      log.error("Unknown even type: " + articleEvent.getClass());
      return article;
    }
  }
}
