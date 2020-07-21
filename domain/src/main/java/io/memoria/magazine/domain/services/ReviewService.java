package io.memoria.magazine.domain.services;

import io.memoria.magazine.domain.model.review.ReviewCmd.CreateContentReview;
import io.memoria.magazine.domain.model.review.ReviewEvent.ReviewCreated;
import io.memoria.magazine.domain.model.review.ReviewEvent.ReviewFulfilled;
import io.memoria.magazine.domain.model.review.ReviewEvent.ReviewResolved;
import reactor.core.publisher.Mono;

public interface ReviewService {

  Mono<ReviewCreated> create(String id, CreateContentReview createContentReview);

  Mono<ReviewFulfilled> fulfill(String reviewId);

  Mono<ReviewResolved> resolve(String reviewId);
}
