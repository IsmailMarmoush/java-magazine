package io.memoria.magazine.domain.model.suggestion;

import io.memoria.magazine.domain.model.article.ArticleEvent;

public interface SuggestionEvent extends ArticleEvent {
  record SuggestionCreated(String suggestionId, String articleId, String newContent) implements SuggestionEvent {}

  record SuggestionFulfilled(String suggestionId, String articleId) implements SuggestionEvent {}

  record SuggestionResolved(String suggestionId, String articleId) implements SuggestionEvent {}

  String suggestionId();
}
