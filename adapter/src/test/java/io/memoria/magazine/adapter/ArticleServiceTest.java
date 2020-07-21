package io.memoria.magazine.adapter;

import io.memoria.magazine.domain.model.article.ArticleEventHandler;
import io.memoria.magazine.adapter.repo.EventRepo;
import io.memoria.magazine.adapter.repo.memory.InMemoryEventRepo;
import io.memoria.magazine.adapter.service.DefaultArticleService;
import io.memoria.magazine.domain.model.article.Article;
import io.memoria.magazine.domain.services.ArticleService;
import io.memoria.magazine.domain.model.article.ArticleEvent;
import io.vavr.collection.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static io.memoria.magazine.adapter.ArticleTestData.ARTICLE_CONTENT;
import static io.memoria.magazine.adapter.ArticleTestData.ARTICLE_CREATED;
import static io.memoria.magazine.adapter.ArticleTestData.ARTICLE_ID;
import static io.memoria.magazine.adapter.ArticleTestData.ARTICLE_PUBLISHED;
import static io.memoria.magazine.adapter.ArticleTestData.NEW_TITLE;
import static io.memoria.magazine.adapter.ArticleTestData.TITLE_EDITED;
import static io.memoria.magazine.domain.model.article.ArticleStatus.PUBLISHED;

public class ArticleServiceTest {
  private final EventRepo<ArticleEvent> repo;
  private final ArticleService service;
  private final Map<String, List<ArticleEvent>> map = new HashMap<>();

  public ArticleServiceTest() {
    this.repo = new InMemoryEventRepo<>(map);
    this.service = new DefaultArticleService(this.repo, new ArticleEventHandler());
  }

  @BeforeEach
  public void afterEach() {
    map.clear();
  }

  @Test
  @DisplayName("ArticleService commands should produce same article data eventually")
  public void commandsTest() {
    var createArticleMono = service.create(ARTICLE_ID, ArticleTestData.CREATE_ARTICLE_DRAFT)
                                   .flatMap(event -> repo.add(event.articleId(), event));
    var editArticle = service.edit(ArticleTestData.EDIT_ARTICLE_TITLE)
                             .flatMap(event -> repo.add(event.articleId(), event));
    var publishArticle = service.publish(ArticleTestData.ARTICLE_ID)
                                .flatMap(event -> repo.add(event.articleId(), event));
    var events = repo.stream(ArticleTestData.ARTICLE_ID).reduce(Article.empty(), new ArticleEventHandler());
    var result = createArticleMono.then(editArticle).then(publishArticle).then(events);
    StepVerifier.create(result)
                .expectNext(new Article(ArticleTestData.NEW_TITLE, ARTICLE_CONTENT, PUBLISHED))
                .expectComplete()
                .verify();
  }

  @Test
  @DisplayName("EventHandler should produce expected article updates")
  public void eventHandlerTest() {
    map.put(ArticleTestData.ARTICLE_ID, List.of(ARTICLE_CREATED, TITLE_EDITED, ARTICLE_PUBLISHED));
    var eventFlux = this.repo.stream(ArticleTestData.ARTICLE_ID);
    var articleMono = eventFlux.reduce(Article.empty(), new ArticleEventHandler());
    StepVerifier.create(articleMono)
                .expectNext(new Article(NEW_TITLE, ARTICLE_CONTENT, PUBLISHED))
                .expectComplete()
                .verify();
  }

}
