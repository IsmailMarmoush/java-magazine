package io.memoria.magazine.domain.model.review;

import io.memoria.magazine.domain.model.article.ArticleEvent;

public interface ReviewEvent extends ArticleEvent {
  record ReviewCreated(String articleId, String reviewId, String newContent) implements ReviewEvent {}

  record ReviewFulfilled(String articleId, String reviewId) implements ReviewEvent {}

  record ReviewResolved(String articleId, String reviewId) implements ReviewEvent {}

  String reviewId();
}
