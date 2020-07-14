package io.memoria.magazine.core.domain;

public record ArticleContentSuggestion(String originalArticleId,
                                       String newContent,
                                       SuggestionStatus suggestionStatus) {}
