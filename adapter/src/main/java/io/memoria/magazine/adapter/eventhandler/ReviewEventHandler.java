package io.memoria.magazine.adapter.eventhandler;

import io.memoria.jutils.eventsourcing.event.EventHandler;
import io.memoria.magazine.core.domain.Review;
import io.memoria.magazine.core.services.dto.ReviewEvent;
import io.memoria.magazine.core.services.dto.ReviewEvent.ContentReviewCreated;
import io.memoria.magazine.core.services.dto.ReviewEvent.ReviewFulfilled;
import io.memoria.magazine.core.services.dto.ReviewEvent.ReviewResolved;

import static io.memoria.magazine.core.domain.ReviewStatus.CREATED;
import static io.memoria.magazine.core.domain.ReviewType.CONTENT_CHANGE;

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
