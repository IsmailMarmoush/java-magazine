package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.event.Event;
import io.memoria.magazine.domain.model.Topic;
import io.vavr.collection.Set;

public interface ArticleEvent extends Event {
  record DraftArticleSubmitted(String articleId, String creatorId, String title, String content, Set<Topic>topics)
          implements ArticleEvent {}

  record ArticlePublished(String articleId) implements ArticleEvent {}

  record ArticleTitleEdited(String articleId, String newTitle) implements ArticleEvent {}

  String articleId();
}
