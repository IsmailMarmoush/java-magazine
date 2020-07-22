package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.event.EventHandler;
import io.memoria.magazine.domain.model.Topic;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticlePublished;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;
import io.memoria.magazine.domain.model.article.ArticleEvent.DraftArticleSubmitted;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.article.ArticleStatus.DRAFT;
import static io.memoria.magazine.domain.model.article.ArticleStatus.PUBLISHED;
import static org.assertj.core.api.Assertions.assertThat;

public class ArticleEventHandlerTest {
  private static EventHandler<Article, ArticleEvent> handler = new ArticleEventHandler();
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

  @Test
  @DisplayName("DraftArticleSubmitted event should produce a draft article with same data")
  public void draftArticleSubmittedEvent() {
    // When
    var article = handler.apply(Article.empty(), ARTICLE_SUBMITTED);
    // Then
    assertThat(article.isEmpty()).isFalse();
    assertThat(article.id()).isEqualTo(ARTICLE_SUBMITTED.articleId());
    assertThat(article.creatorId()).isEqualTo(ARTICLE_SUBMITTED.creatorId());
    assertThat(article.title()).isEqualTo(ARTICLE_SUBMITTED.title());
    assertThat(article.content()).isEqualTo(ARTICLE_SUBMITTED.content());
    assertThat(article.status()).isEqualTo(DRAFT);
    assertThat(article.topics()).isEqualTo(ARTICLE_SUBMITTED.topics());
    assertThat(article.suggestions()).isEmpty();
  }

  @Test
  @DisplayName("ArticlePublished event should produce an article with published status")
  public void publishedEvent() {
    // When
    var article = handler.apply(Article.empty(), List.of(ARTICLE_SUBMITTED, ARTICLE_PUBLISHED));
    // Then
    assertThat(article.status()).isEqualTo(PUBLISHED);
  }

  @Test
  @DisplayName("ArticleTitleEdited event should produce an article with the new title")
  public void titleEditedEvent() {
    // When
    var article = handler.apply(Article.empty(), List.of(ARTICLE_SUBMITTED, TITLE_EDITED));
    // Then
    assertThat(article.title()).isEqualTo(TITLE_EDITED.newTitle());
  }
}
