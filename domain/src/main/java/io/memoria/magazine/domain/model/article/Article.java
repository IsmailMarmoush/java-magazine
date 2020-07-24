package io.memoria.magazine.domain.model.article;

import io.memoria.magazine.domain.model.Topic;
import io.memoria.magazine.domain.model.suggestion.Suggestion;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;
import io.vavr.control.Option;

import static io.memoria.magazine.domain.model.suggestion.SuggestionStatus.RESOLVED;

public record Article(String id,
                      String creatorId,
                      String title,
                      String content,
                      ArticleStatus status,
                      Set<Topic>topics,
                      Set<Suggestion>suggestions,
                      Set<String>reviewers) {
  public static Article empty() {
    return new Article("", "", "", "", ArticleStatus.DRAFT, HashSet.empty(), HashSet.empty(), HashSet.empty());
  }

  public boolean allSuggestionsResolved() {
    return this.suggestions.filter(s -> !s.status().equals(RESOLVED)).size() == 0;
  }

  public Option<Suggestion> findSuggestion(String suggestionId) {
    return this.suggestions.find(s -> s.id().equals(suggestionId));
  }

  public boolean is(ArticleStatus status) {
    return this.status.equals(status);
  }

  public boolean isEmpty() {
    return this.equals(empty());
  }

  public Article toPublished() {
    return new Article(this.id,
                       this.creatorId,
                       this.title,
                       this.content,
                       ArticleStatus.PUBLISHED,
                       this.topics,
                       this.suggestions,
                       this.reviewers);
  }

  public Article withContent(String content) {
    return new Article(this.id,
                       this.creatorId,
                       this.title,
                       content,
                       this.status,
                       this.topics,
                       this.suggestions,
                       this.reviewers);
  }

  public Article withReviewer(String id) {
    return new Article(this.id,
                       this.creatorId,
                       this.title,
                       this.content,
                       this.status,
                       this.topics,
                       this.suggestions,
                       this.reviewers.add(id));
  }

  public Article withSuggestion(Suggestion suggestion) {
    return new Article(this.id,
                       this.creatorId,
                       this.title,
                       this.content,
                       this.status,
                       this.topics,
                       this.suggestions.add(suggestion),
                       this.reviewers);
  }

  public Article withTitle(String title) {
    return new Article(this.id,
                       this.creatorId,
                       title,
                       this.content,
                       this.status,
                       this.topics,
                       this.suggestions,
                       this.reviewers);
  }

  public Article withTopic(Topic topic) {
    return new Article(this.id,
                       this.creatorId,
                       this.title,
                       this.content,
                       this.status,
                       this.topics.add(topic),
                       this.suggestions,
                       this.reviewers);
  }
}
