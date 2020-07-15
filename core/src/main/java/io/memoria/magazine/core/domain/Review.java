package io.memoria.magazine.core.domain;

import static io.memoria.magazine.core.domain.ReviewStatus.CREATED;
import static io.memoria.magazine.core.domain.ReviewType.CONTENT_CHANGE;

public record Review(String articleId,
                     String newContent,
                     ReviewType reviewType,
                     ReviewStatus reviewStatus) {
  public static Review empty() {
    return new Review("", "", CONTENT_CHANGE, CREATED);
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
