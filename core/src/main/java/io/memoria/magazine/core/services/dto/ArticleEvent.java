package io.memoria.magazine.core.services.dto;

import io.memoria.jutils.eventsourcing.event.Event;

import java.time.LocalDateTime;

public interface ArticleEvent extends Event {
  record ArticleCreated(String articleId, String title, String content, LocalDateTime time) implements ArticleEvent {}

  record ArticlePublished(String articleId, LocalDateTime time) implements ArticleEvent {}

  record ArticleTitleEdited(String articleId, String newTitle, LocalDateTime time) implements ArticleEvent {}

  String articleId();
}
