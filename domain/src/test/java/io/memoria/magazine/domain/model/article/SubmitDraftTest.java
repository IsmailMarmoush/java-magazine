package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.cmd.CommandHandler;
import io.memoria.magazine.domain.model.Topic;
import io.memoria.magazine.domain.model.article.ArticleCmd.SubmitDraft;
import io.memoria.magazine.domain.model.article.ArticleEvent.DraftArticleSubmitted;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.MagazineError.InvalidArticleState.EMPTY_TOPICS;
import static io.memoria.magazine.domain.model.MagazineError.UnauthorizedError.UNAUTHORIZED;
import static io.memoria.magazine.domain.model.Tests.BOB_JOURNALIST;
import static io.memoria.magazine.domain.model.Tests.RAY_CHIEF_EDITOR;
import static org.assertj.core.api.Assertions.assertThat;

public class SubmitDraftTest {
  private static final CommandHandler<Article, ArticleCmd, ArticleEvent> handler = new ArticleCommandHandler();

  private static final String articleId = "3";
  private static final String title = "Logic Programming";
  private static final String content = "Prolog is an awesome lang";
  private static final Set<Topic> topics = HashSet.of(new Topic("LP"));

  @Test
  @DisplayName("Non journalists shouldn't be able to submit drafts")
  public void isJournalist() {
    var submitDraft = new SubmitDraft(RAY_CHIEF_EDITOR, articleId, title, content, HashSet.empty());
    var tryingToSubmitDraft = handler.apply(Article.empty(), submitDraft);
    assertThat(tryingToSubmitDraft.isFailure()).isTrue();
    assertThat(tryingToSubmitDraft.getCause()).isEqualTo(UNAUTHORIZED);
  }

  @Test
  @DisplayName("Article should have at least one topic when submitted as draft")
  public void notEmptyTopics() {
    var submitDraft = new SubmitDraft(BOB_JOURNALIST, articleId, title, content, HashSet.empty());
    var tryingToSubmitDraft = handler.apply(Article.empty(), submitDraft);
    assertThat(tryingToSubmitDraft.isFailure()).isTrue();
    assertThat(tryingToSubmitDraft.getCause()).isEqualTo(EMPTY_TOPICS);
  }

  @Test
  @DisplayName("A journalist should be able to submit article draft successfully")
  public void submitDraft() {
    var submitDraft = new SubmitDraft(BOB_JOURNALIST, articleId, title, content, topics);
    var tryingToSubmitDraft = handler.apply(Article.empty(), submitDraft);
    assertThat(tryingToSubmitDraft.isSuccess()).isTrue();
    assertThat(tryingToSubmitDraft.get()).contains(new DraftArticleSubmitted(articleId,
                                                                             BOB_JOURNALIST.id(),
                                                                             title,
                                                                             content,
                                                                             topics));
  }
}
