package io.memoria.magazine.core.services.dto;

import io.memoria.jutils.eventsourcing.event.Event;

public interface ReviewEvent extends Event {
  record ContentReviewCreated(String reviewId, String articleId, String newContent)
          implements ReviewEvent {}

  record ReviewFulfilled(String reviewId) implements ReviewEvent {}

  record ReviewResolved(String reviewId) implements ReviewEvent {}
  
  String reviewId();
}
