package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.cmd.CommandHandler;
import io.memoria.magazine.domain.model.MagazineError.InvalidArticleState;
import io.memoria.magazine.domain.model.MagazineError.UnauthorizedError;
import io.memoria.magazine.domain.model.article.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.Tests.ALEX_JOURNALIST;
import static io.memoria.magazine.domain.model.Tests.BOB_JOURNALIST;
import static io.memoria.magazine.domain.model.Tests.BOB_OOP_ARTICLE;
import static io.memoria.magazine.domain.model.Tests.RAY_CHIEF_EDITOR;
import static org.assertj.core.api.Assertions.assertThat;

public class EditArticleTest {
  private static final String NEW_TITLE = "Object Oriented Design";
  private static final CommandHandler<Article, ArticleCmd, ArticleEvent> handler = new ArticleCommandHandler();

  @Test
  @DisplayName("Owner journalist should edit article successfully")
  public void editArticleTitle() {
    var editArticleCmd = new EditArticleTitle(BOB_JOURNALIST, BOB_OOP_ARTICLE.id(), NEW_TITLE);
    var tryingToEditArticle = handler.apply(BOB_OOP_ARTICLE, editArticleCmd);
    assertThat(tryingToEditArticle.isSuccess()).isTrue();
    assertThat(tryingToEditArticle.get()).contains(new ArticleTitleEdited(BOB_OOP_ARTICLE.id(), NEW_TITLE));
  }

  @Test
  @DisplayName("Non journalist editing article should throw unauthorized exception")
  public void journalist() {
    var editArticleCmd = new EditArticleTitle(RAY_CHIEF_EDITOR, BOB_OOP_ARTICLE.id(), NEW_TITLE);
    var tryingToEditArticle = handler.apply(BOB_OOP_ARTICLE, editArticleCmd);
    assertThat(tryingToEditArticle.isFailure()).isTrue();
    assertThat(tryingToEditArticle.getCause()).isExactlyInstanceOf(UnauthorizedError.class);
  }

  @Test
  @DisplayName("Article should not be empty")
  public void notEmpty() {
    var editArticleCmd = new EditArticleTitle(BOB_JOURNALIST, BOB_OOP_ARTICLE.id(), NEW_TITLE);
    var tryingToEditArticle = handler.apply(Article.empty(), editArticleCmd);
    assertThat(tryingToEditArticle.isFailure()).isTrue();
    assertThat(tryingToEditArticle.getCause()).isEqualTo(InvalidArticleState.EMPTY_ARTICLE);
  }

  @Test
  @DisplayName("Journalists cannot change each other's drafts")
  public void ownersOnly() {
    var editArticleCmd = new EditArticleTitle(ALEX_JOURNALIST, BOB_OOP_ARTICLE.id(), NEW_TITLE);
    var tryingToEditArticle = handler.apply(BOB_OOP_ARTICLE, editArticleCmd);
    assertThat(tryingToEditArticle.isFailure()).isTrue();
    assertThat(tryingToEditArticle.getCause()).isExactlyInstanceOf(UnauthorizedError.class);
  }
}
