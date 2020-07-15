package io.memoria.magazine.core.domain;

public record Article(String title, String content, ArticleStatus status) {
  public static Article empty() {
    return new Article("", "", ArticleStatus.DRAFT);
  }

  public Article toPublished() {
    return new Article(this.title, this.content, ArticleStatus.PUBLISHED);
  }

  public Article withContent(String content) {
    return new Article(this.title, content, this.status);
  }

  public Article withTitle(String title) {
    return new Article(title, this.content, this.status);
  }
}
