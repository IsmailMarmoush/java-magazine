package io.memoria.magazine.domain.model.article;

import io.memoria.magazine.domain.model.Topic;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticlePublished;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;
import io.memoria.magazine.domain.model.article.ArticleEvent.DraftArticleSubmitted;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

public class ArticleEventsTestData {
  public static final Set<Topic> TOPICS;
  public static final DraftArticleSubmitted ARTICLE_SUBMITTED;
  public static final ArticleTitleEdited TITLE_EDITED;
  public static final ArticlePublished ARTICLE_PUBLISHED;

  static {
    // Given
    TOPICS = HashSet.of(new Topic("oop"), new Topic("programming"));
    ARTICLE_SUBMITTED = new DraftArticleSubmitted("1",
                                                  "bob",
                                                  "Introduction to oop programming",
                                                  "Welcome to OOP",
                                                  TOPICS);
    TITLE_EDITED = new ArticleTitleEdited("1", "Reactive Programming");
    ARTICLE_PUBLISHED = new ArticlePublished("1");
  }

  private ArticleEventsTestData() {}
}
