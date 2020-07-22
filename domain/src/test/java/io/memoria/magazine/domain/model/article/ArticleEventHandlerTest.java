package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.event.EventHandler;
import io.vavr.collection.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.memoria.magazine.domain.model.article.ArticleEventsTestData.ARTICLE_PUBLISHED;
import static io.memoria.magazine.domain.model.article.ArticleEventsTestData.ARTICLE_SUBMITTED;
import static io.memoria.magazine.domain.model.article.ArticleEventsTestData.TITLE_EDITED;
import static io.memoria.magazine.domain.model.article.ArticleStatus.DRAFT;
import static io.memoria.magazine.domain.model.article.ArticleStatus.PUBLISHED;
import static org.assertj.core.api.Assertions.assertThat;

public class ArticleEventHandlerTest {
  private static EventHandler<Article, ArticleEvent> handler = new ArticleEventHandler();

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
