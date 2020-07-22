package io.memoria.magazine.domain.model;

import io.memoria.magazine.domain.model.suggestion.SuggestionStatus;

public class MagazineError extends Exception {
  public static class InvalidArticleState extends MagazineError {
    public static final InvalidArticleState EMPTY_ARTICLE = new InvalidArticleState("Article is empty");
    public static final InvalidArticleState EMPTY_TOPICS = new InvalidArticleState("Article doesn't have topics");
    public static final InvalidArticleState ARTICLE_ALREADY_PUBLISHED = new InvalidArticleState(
            "Article was already published");
    public static final InvalidArticleState SUGGESTIONS_NOT_RESOLVED = new InvalidArticleState(
            "Some suggestions aren't resolved yet");

    public InvalidArticleState(String message) {
      super(message);
    }
  }

  public static class InvalidSuggestionState extends MagazineError {
    public static final InvalidArticleState EMPTY_COMMENT = new InvalidArticleState("Comment is empty");

    private InvalidSuggestionState(String message) {
      super(message);
    }
  }

  public static class InvalidSuggestionStatus extends MagazineError {
    public InvalidSuggestionStatus(SuggestionStatus correctStatus) {
      super("Suggestion should be " + correctStatus.name() + " first");
    }

    public InvalidSuggestionStatus(String message) {
      super(message);
    }
  }

  public static class NotFoundError extends MagazineError {
    public static final NotFoundError NOT_FOUND = new NotFoundError("Not found");

    private NotFoundError(String message) {
      super(message);
    }
  }

  public static class UnauthorizedError extends MagazineError {
    public static final UnauthorizedError UNAUTHORIZED = new UnauthorizedError("Unauthorized");

    public UnauthorizedError(String message) {
      super(message);
    }
  }

  public static class UnsupportedCommand extends MagazineError {
    public static final UnsupportedCommand UNSUPPORTED_COMMAND = new UnsupportedCommand("Command is not supported");

    public UnsupportedCommand(String message) {
      super(message);
    }
  }

  private MagazineError(String message) {
    super(message);
  }
}
