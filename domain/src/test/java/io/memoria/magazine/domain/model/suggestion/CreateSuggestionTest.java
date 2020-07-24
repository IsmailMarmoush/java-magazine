package io.memoria.magazine.domain.model.suggestion;

import io.memoria.magazine.domain.model.article.Article;
import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.CreateSuggestion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.MagazineError.InvalidArticleState.ARTICLE_ALREADY_PUBLISHED;
import static io.memoria.magazine.domain.model.MagazineError.InvalidSuggestionState.EMPTY_COMMENT;
import static io.memoria.magazine.domain.model.MagazineError.UnauthorizedError.UNAUTHORIZED;
import static io.memoria.magazine.domain.model.Tests.ALEX_JOURNALIST;
import static io.memoria.magazine.domain.model.Tests.BOB_OOP_ARTICLE;
import static io.memoria.magazine.domain.model.Tests.SAM_COPYWRITER;
import static org.assertj.core.api.Assertions.assertThat;

public class CreateSuggestionTest {
  private static final SuggestionCommandHandler handler = new SuggestionCommandHandler();
  private static final String suggestionId = "1";
  private static final String articleId = "1000";
  private static final String comment = "Fix grammar, please";

  @Test
  @DisplayName("Copywriters can only suggest changes to article they were assigned to")
  public void createSuggestion() {
    var createSuggestion = new CreateSuggestion(SAM_COPYWRITER, suggestionId, articleId, comment);
    var tryingToCreateSuggestion = handler.apply(Article.empty().withReviewer(SAM_COPYWRITER.id()), createSuggestion);
    assertThat(tryingToCreateSuggestion.isSuccess()).isTrue();
  }

  @Test
  @DisplayName("Only copywriters can create suggestions")
  public void isCopywriter() {
    var createSuggestion = new CreateSuggestion(ALEX_JOURNALIST, suggestionId, articleId, comment);
    var tryingToCreateSuggestion = handler.apply(Article.empty().withReviewer(SAM_COPYWRITER.id()), createSuggestion);
    assertThat(tryingToCreateSuggestion.isFailure()).isTrue();
    assertThat(tryingToCreateSuggestion.getCause()).isEqualTo(UNAUTHORIZED);
  }

  @Test
  @DisplayName("Suggested changes can be considered as comments to the whole article. They should be non-empty chunks of text")
  public void nonEmptySuggestion() {
    var createSuggestion = new CreateSuggestion(SAM_COPYWRITER, suggestionId, articleId, "");
    var tryingToCreateSuggestion = handler.apply(Article.empty().withReviewer(SAM_COPYWRITER.id()), createSuggestion);
    assertThat(tryingToCreateSuggestion.isFailure()).isTrue();
    assertThat(tryingToCreateSuggestion.getCause()).isEqualTo(EMPTY_COMMENT);
  }

  @Test
  @DisplayName("Once the article is published, suggestions are no longer allowed.")
  public void suggestionsAreNotAllowed() {
    var createSuggestion = new CreateSuggestion(SAM_COPYWRITER, suggestionId, articleId, comment);
    var tryingToCreateSuggestion = handler.apply(BOB_OOP_ARTICLE.toPublished().withReviewer(SAM_COPYWRITER.id()),
                                                 createSuggestion);
    assertThat(tryingToCreateSuggestion.isFailure()).isTrue();
    assertThat(tryingToCreateSuggestion.getCause()).isEqualTo(ARTICLE_ALREADY_PUBLISHED);
  }
}
