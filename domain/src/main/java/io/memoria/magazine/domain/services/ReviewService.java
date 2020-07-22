package io.memoria.magazine.domain.services;

import io.memoria.magazine.domain.model.review.ReviewCmd.CreateReview;
import io.memoria.magazine.domain.model.review.ReviewEvent.ReviewCreated;
import io.memoria.magazine.domain.model.review.ReviewEvent.ReviewFulfilled;
import io.memoria.magazine.domain.model.review.ReviewEvent.ReviewResolved;
import reactor.core.publisher.Mono;

public interface ReviewService {

  Mono<ReviewCreated> create(String id, CreateReview createReview);

  Mono<ReviewFulfilled> fulfill(String reviewId);

  Mono<ReviewResolved> resolve(String reviewId);
}
