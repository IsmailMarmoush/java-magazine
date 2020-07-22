package io.memoria.magazine.domain.model.suggestion;

import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.ResolveSuggestion;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionResolved;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.Tests.BOB_OOP_ARTICLE;
import static io.memoria.magazine.domain.model.Tests.SUSAN_EDITOR;
import static org.assertj.core.api.Assertions.assertThat;

public class ResolveSuggestionTest {
  private static final SuggestionCommandHandler handler = new SuggestionCommandHandler();
  private static final String suggestionId = "1";
  private static final String comment = "Fix grammar, please";

  @Test
  @DisplayName("Copywriter should resolve fulfilled suggestion successfully")
  public void resolve() {
    // Given an article with suggestion
    var suggestion = new Suggestion(suggestionId,
                                    comment,
                                    SUSAN_EDITOR.id(),
                                    BOB_OOP_ARTICLE.id(),
                                    SuggestionStatus.FULFILLED);
    var articleWithSuggestion = BOB_OOP_ARTICLE.withSuggestion(suggestion);
    // When resolve command is applied
    var respond = new ResolveSuggestion(SUSAN_EDITOR, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(articleWithSuggestion, respond);
    // Then suggestion should be resolved successfully
    assertThat(tryingToRespond.isSuccess()).isTrue();
    assertThat(tryingToRespond.get()).contains(new SuggestionResolved(suggestionId,
                                                                      SUSAN_EDITOR.id(),
                                                                      BOB_OOP_ARTICLE.id()));
  }

  @Test
  @DisplayName("Suggestion resolver should be a copywriter")
  public void shouldBeCopywriter() {

  }

  @Test
  @DisplayName("Suggestion should be in fulfilled status")
  public void shouldBeFulfilledMode() {

  }

  @Test
  @DisplayName("Suggestion should exist already")
  public void shouldExist() {

  }

  @Test
  @DisplayName("Suggestion resolver should be the suggestion creator")
  public void suggestionOwner() {

  }
}
