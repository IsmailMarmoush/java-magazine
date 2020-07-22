package io.memoria.magazine.domain.model.suggestion;

import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.CREATED;

public record Suggestion(String articleId, String newContent, SuggestionType suggestionType, SuggestionStatus suggestionStatus) {
  public static Suggestion empty() {
    return new Suggestion("", "", SuggestionType.CONTENT_CHANGE, CREATED);
  }

  public boolean isEmpty() {
    return this.equals(empty());
  }

  public Suggestion toFulfilled() {
    return new Suggestion(this.articleId, this.newContent, this.suggestionType, SuggestionStatus.FULFILLED);
  }

  public Suggestion toResolved() {
    return new Suggestion(this.articleId, this.newContent, this.suggestionType, SuggestionStatus.RESOLVED);
  }
}
