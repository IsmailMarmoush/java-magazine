package io.memoria.magazine.domain.model.suggestion;

import io.memoria.magazine.domain.model.article.ArticleEvent;

public interface SuggestionEvent extends ArticleEvent {
  record SuggestionCreated(String suggestionId, String creatorId, String articleId, String comment)
          implements SuggestionEvent {}

  record SuggestionFulfilled(String suggestionId, String creatorId, String articleId) implements SuggestionEvent {}

  record SuggestionResolved(String suggestionId, String creatorId, String articleId) implements SuggestionEvent {}

  String articleId();

  String creatorId();

  String suggestionId();
}
