package io.memoria.magazine.core.services.dto;

import io.memoria.jutils.eventsourcing.event.Event;

public interface SuggestionEvent extends Event {
  record SuggestionCreated(String suggestionId, String articleId, String newTitle, String newContent)
          implements SuggestionEvent {}

  record SuggestionFulfilled(String suggestionId) implements SuggestionEvent {}

  record SuggestionResolved(String suggestionId) implements SuggestionEvent {}

  String suggestionId();
}
