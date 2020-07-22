package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.cmd.CommandHandler;
import io.memoria.jutils.core.generator.IdGenerator;
import io.memoria.magazine.domain.model.article.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.domain.model.article.ArticleCmd.PublishArticle;
import io.memoria.magazine.domain.model.article.ArticleCmd.SubmitDraft;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticlePublished;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;
import io.memoria.magazine.domain.model.article.ArticleEvent.DraftArticleSubmitted;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static io.memoria.magazine.domain.model.MagazineError.InvalidArticleState.ARTICLE_ALREADY_PUBLISHED;
import static io.memoria.magazine.domain.model.MagazineError.InvalidArticleState.EMPTY_ARTICLE;
import static io.memoria.magazine.domain.model.MagazineError.InvalidArticleState.EMPTY_TOPICS;
import static io.memoria.magazine.domain.model.MagazineError.UnauthorizedError.UNAUTHORIZED;
import static io.memoria.magazine.domain.model.MagazineError.UnsupportedCommand.UNSUPPORTED_COMMAND;
import static io.memoria.magazine.domain.model.article.ArticleStatus.PUBLISHED;
import static io.memoria.magazine.domain.services.auth.Role.isAbleTo;

public record ArticleCommandHandler() implements CommandHandler<Article, ArticleCmd, ArticleEvent> {
  private static final Logger log = LoggerFactory.getLogger(ArticleCommandHandler.class.getName());

  @Override
  public Try<List<ArticleEvent>> apply(Article article, ArticleCmd articleCommand) {
    if (articleCommand instanceof SubmitDraft submitDraft) {
      return ableTo(submitDraft).flatMap(tr -> mustHaveTopics(submitDraft)).flatMap(tr -> createArticle(submitDraft));
    } else if (article.isEmpty()) {
      return Try.failure(EMPTY_ARTICLE);
    } else if (articleCommand instanceof EditArticleTitle editTitle) {
      return ableTo(editTitle).flatMap(tr -> mustBeOwner(article, editTitle)).flatMap(tr -> editTitle(editTitle));
    } else if (articleCommand instanceof PublishArticle publishArticle) {
      return ableTo(publishArticle).flatMap(tr -> mustBeOwner(article, publishArticle))
                                   .flatMap(tr -> notPublished(article))
                                   .flatMap(tr -> publish(publishArticle));
    }
    // TODO test case of unknown commands
    log.error("Unsupported command" + articleCommand.getClass());
    return Try.failure(UNSUPPORTED_COMMAND);
  }

  private Try<List<ArticleEvent>> createArticle(SubmitDraft cmd) {
    var event = new DraftArticleSubmitted(cmd.articleId(),
                                          cmd.principal().id(),
                                          cmd.title(),
                                          cmd.content(),
                                          cmd.topics());
    return Try.success(List.of(event));
  }

  private Try<List<ArticleEvent>> editTitle(EditArticleTitle cmd) {
    return Try.success(List.of(new ArticleTitleEdited(cmd.articleId(), cmd.newTitle())));
  }

  private Try<List<ArticleEvent>> publish(PublishArticle cmd) {
    return Try.success(List.of(new ArticlePublished(cmd.articleId())));
  }

  private static Try<Void> ableTo(ArticleCmd cmd) {
    return (isAbleTo(cmd.principal().roles(), cmd.getClass())) ? Try.success(null) : Try.failure(UNAUTHORIZED);
  }

  private static Try<Void> mustBeOwner(Article article, ArticleCmd cmd) {
    return (article.creatorId().equals(cmd.principal().id())) ? Try.success(null) : Try.failure(UNAUTHORIZED);
  }

  private static Try<Void> mustHaveTopics(SubmitDraft cmd) {
    return (cmd.topics().size() > 0) ? Try.success(null) : Try.failure(EMPTY_TOPICS);
  }

  private static Try<Void> notPublished(Article article) {
    return (!article.status().equals(PUBLISHED)) ? Try.success(null) : Try.failure(ARTICLE_ALREADY_PUBLISHED);
  }
}
