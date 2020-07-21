package io.memoria.magazine.domain.model.review;

import io.memoria.jutils.core.eventsourcing.event.EventHandler;
import io.memoria.magazine.domain.model.review.ReviewEvent.ContentReviewCreated;
import io.memoria.magazine.domain.model.review.ReviewEvent.ReviewFulfilled;
import io.memoria.magazine.domain.model.review.ReviewEvent.ReviewResolved;

import static io.memoria.magazine.domain.model.review.ReviewStatus.CREATED;
import static io.memoria.magazine.domain.model.review.ReviewType.CONTENT_CHANGE;

public record ReviewEventHandler() implements EventHandler<Review, ReviewEvent> {
  @Override
  public Review apply(Review review, ReviewEvent reviewEvent) {
    if (reviewEvent instanceof ContentReviewCreated e) {
      return new Review(e.articleId(), e.newContent(), CONTENT_CHANGE, CREATED);
    } else if (review.isEmpty()) {
      throw new IllegalStateException("Previous state is empty");
    } else if (reviewEvent instanceof ReviewFulfilled) {
      return fulfill(review);
    } else if (reviewEvent instanceof ReviewResolved) {
      return resolve(review);
    }
    throw new UnsupportedOperationException();
  }

  private Review fulfill(Review review) {
    return switch (review.reviewStatus()) {
      case CREATED -> review.toFulfilled();
      case FULFILLED -> throw new IllegalStateException("Previous is already Fulfilled");
      case RESOLVED -> throw new IllegalStateException("Can't fulfill review that is already resolved");
    };
  }

  private Review resolve(Review review) {
    return switch (review.reviewStatus()) {
      case CREATED -> throw new IllegalStateException("Can't resolve review that is not fulfilled");
      case FULFILLED -> review.toResolved();
      case RESOLVED -> throw new IllegalStateException("Review is already Resolved");
    };
  }
}
