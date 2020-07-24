package io.memoria.magazine.domain.model.suggestion;

import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.CREATED;

public record Suggestion(String id, String comment, String creatorId, String articleId, SuggestionStatus status) {
  public static Suggestion empty() {
    return new Suggestion("", "", "", "", CREATED);
  }

  public boolean isEmpty() {
    return this.equals(empty());
  }

  public Suggestion toFulfilled() {
    return new Suggestion(this.id, this.comment, this.creatorId, this.articleId, SuggestionStatus.FULFILLED);
  }

  public Suggestion toResolved() {
    return new Suggestion(this.id, this.comment, this.creatorId, this.articleId, SuggestionStatus.RESOLVED);
  }
}
