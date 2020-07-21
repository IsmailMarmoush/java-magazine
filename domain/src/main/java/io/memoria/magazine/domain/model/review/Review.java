package io.memoria.magazine.domain.model.review;

import static io.memoria.magazine.domain.model.review.ReviewStatus.CREATED;

public record Review(String articleId, String newContent, ReviewType reviewType, ReviewStatus reviewStatus) {
  public static Review empty() {
    return new Review("", "", ReviewType.CONTENT_CHANGE, CREATED);
  }

  public boolean isEmpty() {
    return this.equals(empty());
  }

  public Review toFulfilled() {
    return new Review(this.articleId, this.newContent, this.reviewType, ReviewStatus.FULFILLED);
  }

  public Review toResolved() {
    return new Review(this.articleId, this.newContent, this.reviewType, ReviewStatus.RESOLVED);
  }
}
