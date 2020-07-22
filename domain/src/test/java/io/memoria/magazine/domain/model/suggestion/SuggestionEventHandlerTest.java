package io.memoria.magazine.domain.model.suggestion;

import io.memoria.jutils.core.eventsourcing.event.EventHandler;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionCreated;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionFulfilled;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionResolved;
import io.vavr.collection.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.Tests.SAM_COPYWRITER;
import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.FULFILLED;
import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.RESOLVED;
import static org.assertj.core.api.Assertions.assertThat;

public class SuggestionEventHandlerTest {
  private static final EventHandler<Suggestion, SuggestionEvent> handler = new SuggestionEventHandler();
  private static final SuggestionCreated suggestionCreated = new SuggestionCreated("1",
                                                                                   SAM_COPYWRITER.id(),
                                                                                   "1000",
                                                                                   "Fix grammar error");
  private static final SuggestionFulfilled suggestionFulfilled = new SuggestionFulfilled("2", SAM_COPYWRITER.id(), "1000");
  private static final SuggestionResolved suggestionResolved = new SuggestionResolved("3", SAM_COPYWRITER.id(), "1000");

  @Test
  @DisplayName("CreateSuggestion event should produce a suggestion with same data")
  public void create() {
    // When
    var suggestion = handler.apply(Suggestion.empty(), suggestionCreated);
    // Then
    assertThat(suggestion.isEmpty()).isFalse();
    assertThat(suggestion.id()).isEqualTo(suggestionCreated.suggestionId());
    assertThat(suggestion.status()).isEqualTo(SuggestionStatus.CREATED);
    assertThat(suggestion.articleId()).isEqualTo(suggestionCreated.articleId());
    assertThat(suggestion.comment()).isEqualTo(suggestionCreated.comment());
    assertThat(suggestion.creatorId()).isEqualTo(suggestionCreated.creatorId());
  }

  @Test
  @DisplayName("SuggestionFulfilled event should produce a suggestion with Fulfilled status")
  public void fulfill() {
    // When
    var suggestion = handler.apply(Suggestion.empty(), List.of(suggestionCreated, suggestionFulfilled));
    // Then
    assertThat(suggestion.status()).isEqualTo(FULFILLED);
  }

  @Test
  @DisplayName("SuggestionResolved event should produce a suggestion with resolved status")
  public void resolve() {
    // When
    var suggestion = handler.apply(Suggestion.empty(),
                                   List.of(suggestionCreated, suggestionFulfilled, suggestionResolved));
    // Then
    assertThat(suggestion.status()).isEqualTo(RESOLVED);
  }
}
