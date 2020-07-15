package io.memoria.magazine.adapter;

import io.memoria.magazine.adapter.eventhandler.ArticleEventHandler;
import io.memoria.magazine.adapter.repo.ArticleEventRepo;
import io.memoria.magazine.adapter.repo.memory.InMemoryArticleEventRepo;
import io.memoria.magazine.adapter.service.DefaultArticleService;
import io.memoria.magazine.core.domain.Article;
import io.memoria.magazine.core.services.ArticleService;
import io.memoria.magazine.core.services.dto.ArticleEvent;
import io.vavr.collection.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static io.memoria.magazine.adapter.Tests.ARTICLE_CONTENT;
import static io.memoria.magazine.adapter.Tests.ARTICLE_CREATED;
import static io.memoria.magazine.adapter.Tests.ARTICLE_ID;
import static io.memoria.magazine.adapter.Tests.ARTICLE_PUBLISHED;
import static io.memoria.magazine.adapter.Tests.NEW_TITLE;
import static io.memoria.magazine.adapter.Tests.TITLE_EDITED;
import static io.memoria.magazine.core.domain.ArticleStatus.PUBLISHED;

public class ArticleServiceTest {
  private final ArticleEventRepo articleEventRepo;
  private final ArticleService articleService;
  private final Map<String, List<ArticleEvent>> map = new HashMap<>();

  public ArticleServiceTest() {
    this.articleEventRepo = new InMemoryArticleEventRepo(map);
    this.articleService = new DefaultArticleService(this.articleEventRepo, new ArticleEventHandler());
  }

  @BeforeEach
  public void afterEach() {
    map.clear();
  }

  @Test
  @DisplayName("ArticleService commands should produce same article data eventually")
  public void commandsTest() {
    var createArticleMono = articleService.create(ARTICLE_ID, Tests.CREATE_ARTICLE_DRAFT).doOnNext(saveEvent());
    var editArticle = articleService.edit(Tests.EDIT_ARTICLE_TITLE).doOnNext(saveEvent());
    var publishArticle = articleService.publish(Tests.ARTICLE_ID).doOnNext(saveEvent());
    var events = articleEventRepo.get(Tests.ARTICLE_ID).reduce(Article.empty(), new ArticleEventHandler());
    var result = createArticleMono.then(editArticle).then(publishArticle).then(events);
    StepVerifier.create(result)
                .expectNext(new Article(Tests.NEW_TITLE, ARTICLE_CONTENT, PUBLISHED))
                .expectComplete()
                .verify();
  }

  @Test
  @DisplayName("EventHandler should produce expected article updates")
  public void eventHandlerTest() {
    map.put(Tests.ARTICLE_ID, List.of(ARTICLE_CREATED, TITLE_EDITED, ARTICLE_PUBLISHED));
    var eventFlux = this.articleEventRepo.get(Tests.ARTICLE_ID);
    var articleMono = eventFlux.reduce(Article.empty(), new ArticleEventHandler());
    StepVerifier.create(articleMono)
                .expectNext(new Article(NEW_TITLE, ARTICLE_CONTENT, PUBLISHED))
                .expectComplete()
                .verify();
  }

  private Consumer<ArticleEvent> saveEvent() {
    return event -> {
      var id = event.articleId();
      var db = ((InMemoryArticleEventRepo) this.articleEventRepo).db();
      if (db.get(id) == null || db.get(id).isEmpty()) {
        db.put(id, List.of(event));
      } else {
        db.put(id, db.get(id).append(event));
      }
    };
  }
}
