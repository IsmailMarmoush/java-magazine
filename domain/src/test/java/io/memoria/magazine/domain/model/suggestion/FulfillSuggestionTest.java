package io.memoria.magazine.domain.model.suggestion;

import io.memoria.magazine.domain.model.MagazineError.InvalidSuggestionStatus;
import io.memoria.magazine.domain.model.MagazineError.UnauthorizedError;
import io.memoria.magazine.domain.model.Tests;
import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.RespondToSuggestion;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionFulfilled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.MagazineError.NotFoundError.NOT_FOUND;
import static io.memoria.magazine.domain.model.Tests.BOB_JOURNALIST;
import static io.memoria.magazine.domain.model.Tests.BOB_OOP_ARTICLE;
import static io.memoria.magazine.domain.model.Tests.SAM_COPYWRITER;
import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.CREATED;
import static org.assertj.core.api.Assertions.assertThat;

public class FulfillSuggestionTest {
  private static final SuggestionCommandHandler handler = new SuggestionCommandHandler();
  private static final String suggestionId = "1";
  private static final String comment = "Fix grammar, please";
  private static final Suggestion susanSuggestion = new Suggestion(suggestionId,
                                                                   comment,
                                                                   SAM_COPYWRITER.id(),
                                                                   BOB_OOP_ARTICLE.id(),
                                                                   CREATED);

  @Test
  @DisplayName("Suggestion responder should be the article owner")
  public void articleOwner() {
    // Given an article created by Alex with suggestion from Susan
    var articleWithSuggestion = Tests.ALEX_REACTIVE_ARTICLE.withSuggestion(susanSuggestion);
    // When respond command is applied by Bob
    var respond = new RespondToSuggestion(Tests.BOB_JOURNALIST, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(articleWithSuggestion, respond);
    // Then suggestion should be fulfilled successfully
    assertThat(tryingToRespond.isFailure()).isTrue();
    assertThat(tryingToRespond.getCause()).isExactlyInstanceOf(UnauthorizedError.class);
  }

  @Test
  @DisplayName("Journalist should respond to suggestions by making the suggested changes")
  public void respond() {
    // Given
    var articleWithSuggestion = BOB_OOP_ARTICLE.withSuggestion(susanSuggestion);
    // When respond command is applied
    var respond = new RespondToSuggestion(Tests.BOB_JOURNALIST, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(articleWithSuggestion, respond);
    // Then suggestion should be fulfilled successfully
    assertThat(tryingToRespond.isSuccess()).isTrue();
    assertThat(tryingToRespond.get()).contains(new SuggestionFulfilled(suggestionId,
                                                                       BOB_JOURNALIST.id(),
                                                                       BOB_OOP_ARTICLE.id()));
  }

  @Test
  @DisplayName("Suggestion should be in creation mode")
  public void shouldBeInCreationMode() {
    // Given
    var articleWithSuggestion = BOB_OOP_ARTICLE.withSuggestion(susanSuggestion.toFulfilled());
    // When respond command is applied
    var respond = new RespondToSuggestion(Tests.BOB_JOURNALIST, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(articleWithSuggestion, respond);
    // Then suggestion should be fulfilled successfully
    assertThat(tryingToRespond.isFailure()).isTrue();
    assertThat(tryingToRespond.getCause()).isExactlyInstanceOf(InvalidSuggestionStatus.class);
  }

  @Test
  @DisplayName("Suggestion responder should be a journalist")
  public void shouldBeJournalist() {
    // Given
    var articleWithSuggestion = BOB_OOP_ARTICLE.withSuggestion(susanSuggestion);
    // When respond command is applied
    var respond = new RespondToSuggestion(Tests.SUSAN_EDITOR, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(articleWithSuggestion, respond);
    // Then suggestion should be fulfilled successfully
    assertThat(tryingToRespond.isFailure()).isTrue();
    assertThat(tryingToRespond.getCause()).isExactlyInstanceOf(UnauthorizedError.class);
  }

  @Test
  @DisplayName("Suggestion should exist already")
  public void shouldExist() {
    // Given
    assertThat(BOB_OOP_ARTICLE.suggestions()).isEmpty();
    // When respond command is applied
    var respond = new RespondToSuggestion(BOB_JOURNALIST, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(BOB_OOP_ARTICLE, respond);
    // Then suggestion should be fulfilled successfully
    assertThat(tryingToRespond.isFailure()).isTrue();
    assertThat(tryingToRespond.getCause()).isEqualTo(NOT_FOUND);
  }
}
