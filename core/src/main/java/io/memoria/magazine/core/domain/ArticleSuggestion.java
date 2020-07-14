package io.memoria.magazine.core.domain;

public record ArticleSuggestion(Article original, Article suggested, SuggestionStatus suggestionStatus) {}
