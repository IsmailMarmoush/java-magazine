package io.memoria.magazine.adapter.service;

import io.memoria.jutils.adapter.generator.SerialIdGenerator;
import io.memoria.jutils.core.generator.IdGenerator;
import io.memoria.magazine.adapter.repo.memory.InMemoryEventRepo;
import io.memoria.magazine.domain.model.Topic;
import io.memoria.magazine.domain.model.article.ArticleCommandHandler;
import io.memoria.magazine.domain.model.article.ArticleEvent;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticlePublished;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;
import io.memoria.magazine.domain.model.article.ArticleEvent.DraftArticleSubmitted;
import io.memoria.magazine.domain.model.article.ArticleEventHandler;
import io.memoria.magazine.domain.model.auth.Principal;
import io.memoria.magazine.domain.model.auth.Role;
import io.memoria.magazine.domain.service.ArticleService;
import io.memoria.magazine.domain.service.ArticleServiceDTO;
import io.memoria.magazine.domain.service.ArticleServiceDTO.EditArticleTitleDTO;
import io.memoria.magazine.domain.service.ArticleServiceDTO.PublishArticleDTO;
import io.memoria.magazine.domain.service.ArticleServiceDTO.SubmitDraftDTO;
import io.memoria.magazine.domain.service.EventRepo;
import io.vavr.collection.HashSet;
import io.vavr.collection.List;
import io.vavr.collection.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import static io.memoria.magazine.domain.model.auth.Role.JOURNALIST;

public class ArticleServiceTest {
  private static record Person(String id, Set<Role>roles) implements Principal {}

  private static final AtomicLong atomicLong = new AtomicLong();
  private static final IdGenerator idGenerator = new SerialIdGenerator(atomicLong);
  private static final HashMap<String, List<ArticleEvent>> db = new HashMap<>();
  private static final EventRepo<ArticleEvent> repo = new InMemoryEventRepo<>(db);
  private static final ArticleService service = new DefaultArticleService(idGenerator,
                                                                          repo,
                                                                          new ArticleEventHandler(),
                                                                          new ArticleCommandHandler());
  public static final Person BOB_JOURNALIST = new Person("1", HashSet.of(JOURNALIST));
  private static final Topic DYSTOPIAN_TOPIC = new Topic("Dystopia");
  private static final String title = "Do Androids Dream of Electric Sheep";
  private static final String content = "You will be required to do wrong no matter where you go";
  private static final SubmitDraftDTO submitDraftDTO = new SubmitDraftDTO(BOB_JOURNALIST,
                                                                          title,
                                                                          content,
                                                                          HashSet.of(DYSTOPIAN_TOPIC));
  private static final String ARTICLE_ID = "0";
  private static final String NEW_TITLE = "Burning Chrome";
  private static final EditArticleTitleDTO editArticleTitleDTO = new EditArticleTitleDTO(BOB_JOURNALIST,
                                                                                         ARTICLE_ID,
                                                                                         NEW_TITLE);
  private static final DraftArticleSubmitted draftSubmittedEvent = new DraftArticleSubmitted(ARTICLE_ID,
                                                                                             BOB_JOURNALIST.id,
                                                                                             title,
                                                                                             content,
                                                                                             HashSet.of(DYSTOPIAN_TOPIC));
  private static final ArticleTitleEdited articleTitleEditedEvent = new ArticleTitleEdited(ARTICLE_ID, NEW_TITLE);

  @BeforeEach
  public void beforeEach() {
    atomicLong.set(0);
    db.clear();
  }

  @Test
  @DisplayName("Should create article draft successfully")
  public void submitDraft() {
    // when draft is submitted to service
    var eventsMono = service.submitDraft(submitDraftDTO);
    // then
    StepVerifier.create(eventsMono.thenMany(repo.stream(ARTICLE_ID)))
                .expectNext(draftSubmittedEvent)
                .expectComplete()
                .verify();
  }

  @Test
  @DisplayName("Should edit article title successfully")
  public void editTitle() {
    var eventsFlux = service.submitDraft(submitDraftDTO)
                            .then(service.edit(editArticleTitleDTO))
                            .flux()
                            .flatMap(Flux::fromIterable);
    StepVerifier.create(eventsFlux.thenMany(repo.stream(ARTICLE_ID)))
                .expectNext(draftSubmittedEvent, articleTitleEditedEvent)
                .expectComplete()
                .verify();
  }

  @Test
  @DisplayName("Should edit article title successfully")
  public void publishArticle() {
    var eventsFlux = service.submitDraft(submitDraftDTO)
                            .then(service.edit(editArticleTitleDTO))
                            .then(service.publish(new PublishArticleDTO(BOB_JOURNALIST, ARTICLE_ID)))
                            .flux()
                            .flatMap(Flux::fromIterable);
    StepVerifier.create(eventsFlux.thenMany(repo.stream(ARTICLE_ID)))
                .expectNext(draftSubmittedEvent, articleTitleEditedEvent, new ArticlePublished(ARTICLE_ID))
                .expectComplete()
                .verify();
  }
}
