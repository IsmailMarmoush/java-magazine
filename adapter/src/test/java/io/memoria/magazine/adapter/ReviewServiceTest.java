package io.memoria.magazine.adapter;

import io.memoria.magazine.domain.model.review.ReviewEventHandler;
import io.memoria.magazine.adapter.repo.EventRepo;
import io.memoria.magazine.adapter.repo.memory.InMemoryEventRepo;
import io.memoria.magazine.adapter.service.DefaultReviewService;
import io.memoria.magazine.domain.model.review.Review;
import io.memoria.magazine.domain.model.review.ReviewStatus;
import io.memoria.magazine.domain.model.review.ReviewType;
import io.memoria.magazine.domain.services.ReviewService;
import io.memoria.magazine.domain.model.review.ReviewEvent;
import io.vavr.collection.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static io.memoria.magazine.adapter.ArticleTestData.ARTICLE_ID;
import static io.memoria.magazine.adapter.ReviewTestData.REVIEW_ID;

public class ReviewServiceTest {
  private final EventRepo<ReviewEvent> repo;
  private final ReviewService service;
  private final Map<String, List<ReviewEvent>> map = new HashMap<>();

  public ReviewServiceTest() {
    this.repo = new InMemoryEventRepo<>(map);
    this.service = new DefaultReviewService(this.repo, new ReviewEventHandler());
  }

  @BeforeEach
  public void afterEach() {
    map.clear();
  }

  @Test
  @DisplayName("ReviewService commands should produce same review data eventually")
  public void commandsTest() {
    var create = service.create(REVIEW_ID, ReviewTestData.CREATE_CONTENT_REVIEW)
                                      .flatMap(event -> repo.add(event.reviewId(), event));
    var fulfill = service.fulfill(REVIEW_ID).flatMap(event -> repo.add(event.reviewId(), event));
    var resolve = service.resolve(REVIEW_ID).flatMap(event -> repo.add(event.reviewId(), event));
    var events = repo.stream(REVIEW_ID).reduce(Review.empty(), new ReviewEventHandler());
    var result = create.then(fulfill).then(resolve).then(events);
    StepVerifier.create(result)
                .expectNext(new Review(ARTICLE_ID,
                                       ReviewTestData.SUGGESTED_CONTENT,
                                       ReviewType.CONTENT_CHANGE,
                                       ReviewStatus.RESOLVED))
                .expectComplete()
                .verify();
  }
}
