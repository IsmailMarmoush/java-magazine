package io.memoria.magazine.domain.model.suggestion;

import io.memoria.magazine.domain.model.MagazineError.InvalidSuggestionStatus;
import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.ResolveSuggestion;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionResolved;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.MagazineError.NotFoundError.NOT_FOUND;
import static io.memoria.magazine.domain.model.MagazineError.UnauthorizedError.UNAUTHORIZED;
import static io.memoria.magazine.domain.model.Tests.BOB_OOP_ARTICLE;
import static io.memoria.magazine.domain.model.Tests.KAREN_COPYWRITER;
import static io.memoria.magazine.domain.model.Tests.RAY_CHIEF_EDITOR;
import static io.memoria.magazine.domain.model.Tests.SAM_COPYWRITER;
import static org.assertj.core.api.Assertions.assertThat;

public class ResolveSuggestionTest {
  private static final SuggestionCommandHandler handler = new SuggestionCommandHandler();
  private static final String suggestionId = "1";
  private static final String comment = "Fix grammar, please";
  private static final Suggestion samSuggestion = new Suggestion(suggestionId,
                                                                 comment,
                                                                 SAM_COPYWRITER.id(),
                                                                 BOB_OOP_ARTICLE.id(),
                                                                 SuggestionStatus.FULFILLED);

  @Test
  @DisplayName("Copywriter should resolve fulfilled suggestion successfully")
  public void resolve() {
    // Given an article with suggestion from sam the copywriter
    var articleWithSuggestion = BOB_OOP_ARTICLE.withSuggestion(samSuggestion);
    // When resolve command is applied
    var respond = new ResolveSuggestion(SAM_COPYWRITER, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(articleWithSuggestion, respond);
    // Then suggestion should be resolved successfully
    assertThat(tryingToRespond.isSuccess()).isTrue();
    assertThat(tryingToRespond.get()).contains(new SuggestionResolved(suggestionId,
                                                                      SAM_COPYWRITER.id(),
                                                                      BOB_OOP_ARTICLE.id()));
  }

  @Test
  @DisplayName("Suggestion resolver should be a copywriter")
  public void shouldBeCopywriter() {
    // Given an article with suggestion from sam the copywriter
    var articleWithSuggestion = BOB_OOP_ARTICLE.withSuggestion(samSuggestion);
    // When resolve command is applied
    var respond = new ResolveSuggestion(RAY_CHIEF_EDITOR, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(articleWithSuggestion, respond);
    // Then suggestion should fail
    assertThat(tryingToRespond.isFailure()).isTrue();
    assertThat(tryingToRespond.getCause()).isEqualTo(UNAUTHORIZED);
  }

  @Test
  @DisplayName("Suggestion should be in fulfilled status")
  public void shouldBeFulfilledMode() {
    // Given an article with suggestion from sam the copywriter
    var articleWithSuggestion = BOB_OOP_ARTICLE.withSuggestion(samSuggestion.toResolved());
    // When resolve command is applied
    var respond = new ResolveSuggestion(SAM_COPYWRITER, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(articleWithSuggestion, respond);
    // Then suggestion should fail
    assertThat(tryingToRespond.isFailure()).isTrue();
    assertThat(tryingToRespond.getCause()).isExactlyInstanceOf(InvalidSuggestionStatus.class);
  }

  @Test
  @DisplayName("Suggestion should exist already")
  public void shouldExist() {
    // Given an article with no suggestions
    // When resolve command is applied
    var respond = new ResolveSuggestion(SAM_COPYWRITER, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(BOB_OOP_ARTICLE, respond);
    // Then suggestion should fail
    assertThat(tryingToRespond.isFailure()).isTrue();
    assertThat(tryingToRespond.getCause()).isEqualTo(NOT_FOUND);
  }

  @Test
  @DisplayName("Suggestion resolver should be the suggestion creator")
  public void suggestionOwner() {
    var karenCopywriter = new Suggestion(suggestionId,
                                         comment,
                                         KAREN_COPYWRITER.id(),
                                         BOB_OOP_ARTICLE.id(),
                                         SuggestionStatus.FULFILLED);
    // Given an article with suggestion from Karen
    var articleWithSuggestion = BOB_OOP_ARTICLE.withSuggestion(karenCopywriter);
    // When resolve command is applied by Sam the copywriter
    var respond = new ResolveSuggestion(SAM_COPYWRITER, suggestionId, BOB_OOP_ARTICLE.id());
    var tryingToRespond = handler.apply(articleWithSuggestion, respond);
    // Then suggestion should fail
    assertThat(tryingToRespond.isFailure()).isTrue();
    assertThat(tryingToRespond.getCause()).isEqualTo(UNAUTHORIZED);
  }
}
