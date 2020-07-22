package io.memoria.magazine.domain.model.suggestion;

import io.memoria.jutils.core.eventsourcing.cmd.CommandHandler;
import io.memoria.magazine.domain.model.MagazineError.InvalidSuggestionStatus;
import io.memoria.magazine.domain.model.article.Article;
import io.memoria.magazine.domain.model.article.ArticleEvent;
import io.memoria.magazine.domain.model.auth.Role;
import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.CreateSuggestion;
import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.ResolveSuggestion;
import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.RespondToSuggestion;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionCreated;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionFulfilled;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionResolved;
import io.vavr.Function1;
import io.vavr.collection.List;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.memoria.magazine.domain.model.MagazineError.InvalidArticleState.ARTICLE_ALREADY_PUBLISHED;
import static io.memoria.magazine.domain.model.MagazineError.InvalidSuggestionState.EMPTY_COMMENT;
import static io.memoria.magazine.domain.model.MagazineError.NotFoundError.NOT_FOUND;
import static io.memoria.magazine.domain.model.MagazineError.UnauthorizedError.UNAUTHORIZED;
import static io.memoria.magazine.domain.model.MagazineError.UnsupportedCommand.UNSUPPORTED_COMMAND;
import static io.memoria.magazine.domain.model.article.ArticleStatus.PUBLISHED;
import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.CREATED;
import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.FULFILLED;

public record SuggestionCommandHandler() implements CommandHandler<Article, SuggestionCmd, ArticleEvent> {
  private static final Logger log = LoggerFactory.getLogger(SuggestionCommandHandler.class.getName());

  @Override
  public Try<List<ArticleEvent>> apply(Article article, SuggestionCmd suggestionCmd) {
    if (suggestionCmd instanceof CreateSuggestion cmd) {
      return ableTo(cmd).flatMap(tr -> isOneOfReviewers(article, cmd))
                        .flatMap(tr -> articleNotPublished(article))
                        .flatMap(tr -> notEmptyComment(cmd))
                        .flatMap(tr -> createSuggestion(cmd));
    } else if (suggestionCmd instanceof RespondToSuggestion cmd) {
      return mustBeArticleOwner(article, cmd).flatMap(tr -> suggestionMustExist(article, cmd))
                                             .flatMap(mustBeInStatus(CREATED))
                                             .flatMap(v -> fulfillSuggestion(cmd));
    } else if (suggestionCmd instanceof ResolveSuggestion cmd) {
      return suggestionMustExist(article, cmd).flatMap(mustBeSuggestionOwner(cmd))
                                              .flatMap(mustBeInStatus(FULFILLED))
                                              .flatMap(v -> resolveSuggestion(cmd));
    }
    // TODO test case of unknown commands
    log.error("Unsupported command" + suggestionCmd.getClass());
    return Try.failure(UNSUPPORTED_COMMAND);
  }

  private Try<List<ArticleEvent>> fulfillSuggestion(RespondToSuggestion cmd) {
    return Try.success(List.of(new SuggestionFulfilled(cmd.suggestionId(), cmd.principal().id(), cmd.articleId())));
  }

  private Try<Void> isOneOfReviewers(Article article, SuggestionCmd cmd) {
    return (article.reviewers().contains(cmd.principal().id())) ? Try.success(null) : Try.failure(UNAUTHORIZED);
  }

  private Function1<Suggestion, Try<Suggestion>> mustBeInStatus(SuggestionStatus status) {
    return s -> (s.status().equals(status)) ? Try.success(s) : Try.failure(new InvalidSuggestionStatus(status));
  }

  private Try<Void> notEmptyComment(CreateSuggestion cmd) {
    return (!cmd.comment().isEmpty()) ? Try.success(null) : Try.failure(EMPTY_COMMENT);
  }

  private Try<List<ArticleEvent>> resolveSuggestion(ResolveSuggestion cmd) {
    return Try.success(List.of(new SuggestionResolved(cmd.suggestionId(), cmd.principal().id(), cmd.articleId())));
  }

  private Try<Suggestion> suggestionMustExist(Article article, SuggestionCmd cmd) {
    return article.findSuggestion(cmd.suggestionId()).toTry(() -> NOT_FOUND);
  }

  private static Try<Void> ableTo(SuggestionCmd cmd) {
    return (Role.isAbleTo(cmd.principal().roles(), cmd.getClass())) ? Try.success(null) : Try.failure(UNAUTHORIZED);
  }

  private static Try<Void> articleNotPublished(Article article) {
    return (!article.status().equals(PUBLISHED)) ? Try.success(null) : Try.failure(ARTICLE_ALREADY_PUBLISHED);
  }

  private static Try<List<ArticleEvent>> createSuggestion(CreateSuggestion cmd) {
    var suggestionCreated = new SuggestionCreated(cmd.suggestionId(),
                                                  cmd.principal().id(),
                                                  cmd.articleId(),
                                                  cmd.comment());
    return Try.success(List.of(suggestionCreated));
  }

  private static Try<Void> mustBeArticleOwner(Article article, SuggestionCmd cmd) {
    return article.creatorId().equals(cmd.principal().id()) ? Try.success(null) : Try.failure(UNAUTHORIZED);
  }

  private static Function1<Suggestion, Try<Suggestion>> mustBeSuggestionOwner(SuggestionCmd cmd) {
    return s -> (s.creatorId().equals(cmd.principal().id())) ? Try.success(s) : Try.failure(UNAUTHORIZED);
  }
}
