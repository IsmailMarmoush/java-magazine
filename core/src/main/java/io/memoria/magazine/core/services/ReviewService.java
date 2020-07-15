package io.memoria.magazine.core.services;

import io.memoria.magazine.core.services.dto.ReviewCmd.CreateContentReview;
import io.memoria.magazine.core.services.dto.ReviewEvent.ContentReviewCreated;
import io.memoria.magazine.core.services.dto.ReviewEvent.ReviewFulfilled;
import io.memoria.magazine.core.services.dto.ReviewEvent.ReviewResolved;
import reactor.core.publisher.Mono;

public interface ReviewService {

  Mono<ContentReviewCreated> create(String id, CreateContentReview createContentReview);

  Mono<ReviewFulfilled> fulfill(String reviewId);

  Mono<ReviewResolved> resolve(String reviewId);
}
