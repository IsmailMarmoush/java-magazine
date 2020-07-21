package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.cmd.CommandHandler;
import io.memoria.jutils.core.generator.IdGenerator;
import io.memoria.magazine.domain.model.article.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.domain.model.article.ArticleCmd.PublishArticle;
import io.memoria.magazine.domain.model.article.ArticleCmd.SubmitDraft;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleCreated;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticlePublished;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;
import io.memoria.magazine.domain.services.auth.Role;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

import static io.memoria.magazine.domain.model.MagazineError.InvalidArticleState.ARTICLE_ALREADY_PUBLISHED;
import static io.memoria.magazine.domain.model.MagazineError.InvalidArticleState.EMPTY_ARTICLE;
import static io.memoria.magazine.domain.model.MagazineError.UnauthorizedError.UNAUTHORIZED;
import static io.memoria.magazine.domain.model.MagazineError.UnsupportedCommand.UNSUPPORTED_COMMAND;
import static io.memoria.magazine.domain.model.article.ArticleStatus.DRAFT;
import static io.memoria.magazine.domain.services.auth.Role.JOURNALIST;

public record ArticleCommandHandler(IdGenerator idGen) implements CommandHandler<Article, ArticleCmd, ArticleEvent> {
  private static final Logger log = LoggerFactory.getLogger(ArticleCommandHandler.class.getName());

  @Override
  public Try<List<ArticleEvent>> apply(Article article, ArticleCmd articleCommand) {
    if (articleCommand instanceof SubmitDraft cmd) {
      return mustBe(JOURNALIST, cmd).flatMap(tr -> createArticle(cmd));
    } else if (articleCommand instanceof EditArticleTitle cmd) {
      return notEmpty(article).flatMap(tr -> mustBe(JOURNALIST, cmd))
                              .flatMap(tr -> mustBeOwner(article, cmd))
                              .flatMap(tr -> editTitle(cmd));
    } else if (articleCommand instanceof PublishArticle cmd) {
      return notEmpty(article).flatMap(tr -> mustBe(JOURNALIST, cmd))
                              .flatMap(tr -> mustBeOwner(article, cmd))
                              .flatMap(tr -> publish(cmd, article));
    } else {
      return unknownCommand(articleCommand);
    }
  }

  private Try<List<ArticleEvent>> createArticle(SubmitDraft cmd) {
    var event = new ArticleCreated(cmd.principal().id(),
                                   idGen.get(),
                                   cmd.title(),
                                   cmd.content(),
                                   cmd.topics(),
                                   LocalDateTime.now());
    return Try.success(List.of(event));
  }

  private Try<List<ArticleEvent>> editTitle(EditArticleTitle cmd) {
    var event = new ArticleTitleEdited(cmd.id(), cmd.newTitle(), LocalDateTime.now());
    return Try.success(List.of(event));
  }

  private Try<List<ArticleEvent>> publish(PublishArticle cmd, Article article) {
    if (article.status().equals(DRAFT)) {
      var event = new ArticlePublished(cmd.id(), LocalDateTime.now());
      return Try.success(List.of(event));
    } else {
      return Try.failure(ARTICLE_ALREADY_PUBLISHED);
    }
  }

  private Try<List<ArticleEvent>> unknownCommand(ArticleCmd articleCommand) {
    // TODO test case of unknown commands
    log.error("Unsupported command" + articleCommand.getClass());
    return Try.failure(UNSUPPORTED_COMMAND);
  }

  private static Try<Void> mustBe(Role role, ArticleCmd cmd) {
    if (cmd.principal().roles().contains(role)) {
      return Try.success(null);
    } else {
      return Try.failure(UNAUTHORIZED);
    }
  }

  private static Try<Void> mustBeOwner(Article article, ArticleCmd cmd) {
    return (article.creatorId().equals(cmd.principal().id())) ? Try.success(null) : Try.failure(UNAUTHORIZED);
  }

  private static Try<Void> notEmpty(Article article) {
    return (!article.isEmpty()) ? Try.success(null) : Try.failure(EMPTY_ARTICLE);
  }
}
