package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.cmd.CommandHandler;
import io.memoria.magazine.domain.model.MagazineError.InvalidArticleState;
import io.memoria.magazine.domain.model.MagazineError.InvalidSuggestionStatus;
import io.memoria.magazine.domain.model.MagazineError.UnauthorizedError;
import io.memoria.magazine.domain.model.article.ArticleCmd.PublishArticle;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticlePublished;
import io.memoria.magazine.domain.model.suggestion.Suggestion;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.MagazineError.InvalidArticleState.ARTICLE_ALREADY_PUBLISHED;
import static io.memoria.magazine.domain.model.MagazineError.InvalidArticleState.EMPTY_ARTICLE;
import static io.memoria.magazine.domain.model.Tests.ALEX_JOURNALIST;
import static io.memoria.magazine.domain.model.Tests.BOB_JOURNALIST;
import static io.memoria.magazine.domain.model.Tests.BOB_OOP_ARTICLE;
import static io.memoria.magazine.domain.model.Tests.SAM_COPYWRITER;
import static io.memoria.magazine.domain.model.Tests.SUSAN_EDITOR;
import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.CREATED;
import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.FULFILLED;
import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.RESOLVED;
import static org.assertj.core.api.Assertions.assertThat;

public class PublishArticleTest {
  private static final CommandHandler<Article, ArticleCmd, ArticleEvent> handler = new ArticleCommandHandler();

  @Test
  @DisplayName("Article should not have already been published")
  public void cantPublishTwice() {
    // When published once
    var publishArticle = new PublishArticle(BOB_JOURNALIST, BOB_OOP_ARTICLE.id());
    var firstPublishEvents = handler.apply(BOB_OOP_ARTICLE, publishArticle).get();
    ArticleEventHandler eventHandler = new ArticleEventHandler();
    // And trying to publish again
    var publishedArticle = eventHandler.apply(BOB_OOP_ARTICLE, firstPublishEvents);
    var publishArticleAgain = new PublishArticle(BOB_JOURNALIST, BOB_OOP_ARTICLE.id());
    var tryingToPublishAgain = handler.apply(publishedArticle, publishArticleAgain);
    // Then
    assertThat(tryingToPublishAgain.isFailure()).isTrue();
    assertThat(tryingToPublishAgain.getCause()).isEqualTo(ARTICLE_ALREADY_PUBLISHED);
  }

  @Test
  @DisplayName("Publisher should be a journalist")
  public void journalist() {
    var publishArticle = new PublishArticle(SUSAN_EDITOR, BOB_OOP_ARTICLE.id());
    var events = handler.apply(BOB_OOP_ARTICLE, publishArticle);
    assertThat(events.isFailure()).isTrue();
    assertThat(events.getCause()).isExactlyInstanceOf(UnauthorizedError.class);
  }

  @Test
  @DisplayName("Article should not be empty")
  public void notEmpty() {
    var publishArticle = new PublishArticle(BOB_JOURNALIST, BOB_OOP_ARTICLE.id());
    var tryingToPublishEvents = handler.apply(Article.empty(), publishArticle);
    assertThat(tryingToPublishEvents.isFailure()).isTrue();
    assertThat(tryingToPublishEvents.getCause()).isEqualTo(EMPTY_ARTICLE);
  }

  @Test
  @DisplayName("Publisher should be owner")
  public void ownersOnly() {
    var publishArticle = new PublishArticle(ALEX_JOURNALIST, BOB_OOP_ARTICLE.id());
    var tryingToPublishEvents = handler.apply(BOB_OOP_ARTICLE, publishArticle);
    assertThat(tryingToPublishEvents.isFailure()).isTrue();
    assertThat(tryingToPublishEvents.getCause()).isExactlyInstanceOf(UnauthorizedError.class);
  }

  @Test
  @DisplayName("An owner journalist should publish article successfully")
  public void publishArticle() {
    var publishArticle = new PublishArticle(BOB_JOURNALIST, BOB_OOP_ARTICLE.id());
    var tryingToPublishEvents = handler.apply(BOB_OOP_ARTICLE, publishArticle);
    assertThat(tryingToPublishEvents.isSuccess()).isTrue();
    assertThat(tryingToPublishEvents.get()).contains(new ArticlePublished(BOB_OOP_ARTICLE.id()));
  }

  @Test
  @DisplayName("Journalist, can only publish his article after all suggestions are resolved")
  public void publishArticleWithUn() {
    // Given a non resolved suggestion from susan
    var publishArticle = new PublishArticle(BOB_JOURNALIST, BOB_OOP_ARTICLE.id());
    var susanSuggestion = new Suggestion("1", "some comment", SAM_COPYWRITER.id(), BOB_OOP_ARTICLE.id(), FULFILLED);
    // when trying to publish
    var tryingToPublishEvents = handler.apply(BOB_OOP_ARTICLE.withSuggestion(susanSuggestion), publishArticle);
    // then it fails with invalid article state
    assertThat(tryingToPublishEvents.isFailure()).isTrue();
    assertThat(tryingToPublishEvents.getCause()).isExactlyInstanceOf(InvalidArticleState.class);

    // but when suggestion is resolved
    tryingToPublishEvents = handler.apply(BOB_OOP_ARTICLE.withSuggestion(susanSuggestion.toResolved()), publishArticle);
    // then it passes
    assertThat(tryingToPublishEvents.isSuccess()).isTrue();
    assertThat(tryingToPublishEvents.get()).contains(new ArticlePublished(BOB_OOP_ARTICLE.id()));
  }
}
