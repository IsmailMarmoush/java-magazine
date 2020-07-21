package io.memoria.magazine.domain.model;

public class MagazineError extends Exception {
  private MagazineError(String message) {
    super(message);
  }

  public static class UnauthorizedError extends MagazineError {
    public static final UnauthorizedError UNAUTHORIZED = new UnauthorizedError("Unauthorized");

    public UnauthorizedError(String message) {
      super(message);
    }
  }

  public static class InvalidArticleState extends MagazineError {
    public static final InvalidArticleState EMPTY_ARTICLE = new InvalidArticleState("Article is empty");
    public static final InvalidArticleState ARTICLE_ALREADY_PUBLISHED = new InvalidArticleState(
            "Article was already published");

    public InvalidArticleState(String message) {
      super(message);
    }
  }

  public static class UnsupportedCommand extends MagazineError {
    public static final UnsupportedCommand UNSUPPORTED_COMMAND = new UnsupportedCommand("Command is not supported");

    public UnsupportedCommand(String message) {
      super(message);
    }
  }

}
