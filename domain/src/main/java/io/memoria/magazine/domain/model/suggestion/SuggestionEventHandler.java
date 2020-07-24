package io.memoria.magazine.domain.model.suggestion;

import io.memoria.jutils.core.eventsourcing.event.EventHandler;
import io.memoria.magazine.domain.model.article.ArticleEventHandler;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionCreated;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionFulfilled;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionResolved;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.CREATED;

public record SuggestionEventHandler() implements EventHandler<Suggestion, SuggestionEvent> {
  private static final Logger log = LoggerFactory.getLogger(ArticleEventHandler.class.getName());

  @Override
  public Suggestion apply(Suggestion suggestion, SuggestionEvent suggestionEvent) {
    if (suggestionEvent instanceof SuggestionCreated e) {
      return new Suggestion(e.suggestionId(), e.comment(), e.creatorId(), e.articleId(), CREATED);
    } else if (suggestionEvent instanceof SuggestionFulfilled) {
      return suggestion.toFulfilled();
    } else if (suggestionEvent instanceof SuggestionResolved) {
      return suggestion.toResolved();
    } else {
      log.error("Unknown even type: " + suggestionEvent.getClass());
      return suggestion;
    }
  }
}
