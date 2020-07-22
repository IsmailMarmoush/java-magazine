package io.memoria.magazine.domain.model.suggestion;

import io.memoria.jutils.core.eventsourcing.event.EventHandler;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionCreated;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionFulfilled;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionResolved;

import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.CREATED;
import static io.memoria.magazine.domain.model.suggestion.SuggestionType.CONTENT_CHANGE;

public record SuggestionEventHandler() implements EventHandler<Suggestion, SuggestionEvent> {
  @Override
  public Suggestion apply(Suggestion suggestion, SuggestionEvent suggestionEvent) {
    if (suggestionEvent instanceof SuggestionCreated e) {
      return new Suggestion(e.articleId(), e.newContent(), CONTENT_CHANGE, CREATED);
    } else if (suggestion.isEmpty()) {
      throw new IllegalStateException("Previous state is empty");
    } else if (suggestionEvent instanceof SuggestionFulfilled) {
      return fulfill(suggestion);
    } else if (suggestionEvent instanceof SuggestionResolved) {
      return resolve(suggestion);
    }
    throw new UnsupportedOperationException();
  }

  private Suggestion fulfill(Suggestion suggestion) {
    return switch (suggestion.suggestionStatus()) {
      case CREATED -> suggestion.toFulfilled();
      case FULFILLED -> throw new IllegalStateException("Previous is already Fulfilled");
      case RESOLVED -> throw new IllegalStateException("Can't fulfill suggestion that is already resolved");
    };
  }

  private Suggestion resolve(Suggestion suggestion) {
    return switch (suggestion.suggestionStatus()) {
      case CREATED -> throw new IllegalStateException("Can't resolve suggestion that is not fulfilled");
      case FULFILLED -> suggestion.toResolved();
      case RESOLVED -> throw new IllegalStateException("Review is already Resolved");
    };
  }
}
