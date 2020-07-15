package io.memoria.magazine.core.domain;

public interface Suggestion {
  String articleId();

  record ContentSuggestion(String articleId, String newContent, SuggestionStatus suggestionStatus)
          implements Suggestion {}

  record TitleSuggestion(String articleId, String newTitle, SuggestionStatus suggestionStatus) implements Suggestion {}
}
