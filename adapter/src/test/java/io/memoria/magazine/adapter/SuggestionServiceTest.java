//package io.memoria.magazine.adapter;
//
//import io.memoria.magazine.adapter.repo.EventRepo;
//import io.memoria.magazine.adapter.repo.memory.InMemoryEventRepo;
//import io.memoria.magazine.adapter.service.DefaultSuggestionService;
//import io.memoria.magazine.domain.model.suggestion.Suggestion;
//import io.memoria.magazine.domain.model.suggestion.SuggestionEvent;
//import io.memoria.magazine.domain.model.suggestion.SuggestionEventHandler;
//import io.memoria.magazine.domain.model.suggestion.SuggestionStatus;
//import io.memoria.magazine.domain.model.suggestion.SuggestionType;
//import io.memoria.magazine.adapter.service.SuggestionService;
//import io.vavr.collection.List;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import reactor.test.StepVerifier;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static io.memoria.magazine.adapter.ArticleTestData.ARTICLE_ID;
//import static io.memoria.magazine.adapter.SuggestionTestData.REVIEW_ID;
//
//public class SuggestionServiceTest {
//  private final EventRepo<SuggestionEvent> repo;
//  private final SuggestionService service;
//  private final Map<String, List<SuggestionEvent>> map = new HashMap<>();
//
//  public SuggestionServiceTest() {
//    this.repo = new InMemoryEventRepo<>(map);
//    this.service = new DefaultSuggestionService(this.repo, new SuggestionEventHandler());
//  }
//
//  @BeforeEach
//  public void afterEach() {
//    map.clear();
//  }
//
//  @Test
//  @DisplayName("SuggestionService commands should produce same suggestion data eventually")
//  public void commandsTest() {
//    var create = service.create(REVIEW_ID, SuggestionTestData.CREATE_CONTENT_REVIEW)
//                        .flatMap(event -> repo.add(event.suggestionId(), event));
//    var fulfill = service.fulfill(REVIEW_ID).flatMap(event -> repo.add(event.suggestionId(), event));
//    var resolve = service.resolve(REVIEW_ID).flatMap(event -> repo.add(event.suggestionId(), event));
//    var events = repo.stream(REVIEW_ID).reduce(Suggestion.empty(), new SuggestionEventHandler());
//    var result = create.then(fulfill).then(resolve).then(events);
//    StepVerifier.create(result)
//                .expectNext(new Suggestion(ARTICLE_ID,
//                                       SuggestionTestData.SUGGESTED_CONTENT,
//                                       SuggestionType.CONTENT_CHANGE,
//                                       SuggestionStatus.RESOLVED))
//                .expectComplete()
//                .verify();
//  }
//}
