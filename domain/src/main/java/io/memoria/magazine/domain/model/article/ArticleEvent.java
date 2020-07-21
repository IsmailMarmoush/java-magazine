package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.event.Event;
import io.memoria.magazine.domain.model.Topic;
import io.vavr.collection.Set;

import java.time.LocalDateTime;

public interface ArticleEvent extends Event {
  record ArticleCreated(String articleId,
                        String creatorId,
                        String title,
                        String content,
                        Set<Topic>topics,
                        LocalDateTime time) implements ArticleEvent {}

  record ArticlePublished(String articleId, LocalDateTime time) implements ArticleEvent {}

  record ArticleTitleEdited(String articleId, String newTitle, LocalDateTime time) implements ArticleEvent {}

  String articleId();
}
