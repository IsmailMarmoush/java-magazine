package io.memoria.magazine.domain.model.article;

import io.memoria.magazine.domain.model.Topic;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

public record Article(String id,
                      String creatorId,
                      String title,
                      String content,
                      ArticleStatus status,
                      Set<Topic>topics) {
  public static Article empty() {
    return new Article("", "", "", "", ArticleStatus.DRAFT, HashSet.empty());
  }

  public boolean isEmpty() {
    return this.equals(empty());
  }

  public Article toPublished() {
    return new Article(this.id, this.creatorId, this.title, this.content, ArticleStatus.PUBLISHED, this.topics);
  }

  public Article withContent(String content) {
    return new Article(this.id, this.creatorId, this.title, content, this.status, this.topics);
  }

  public Article withTitle(String title) {
    return new Article(this.id, this.creatorId, title, this.content, this.status, this.topics);
  }

  public Article withTopic(Topic topic) {
    return new Article(this.id, this.creatorId, this.title, this.content, this.status, this.topics.add(topic));
  }
}
