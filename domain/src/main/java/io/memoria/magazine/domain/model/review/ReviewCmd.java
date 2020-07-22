package io.memoria.magazine.domain.model.review;

import io.memoria.jutils.core.eventsourcing.cmd.Command;

public interface ReviewCmd extends Command {

  record CreateReview(String articleId, String newContent) implements ReviewCmd {}

  record ResolveReview(String reviewId, String articleId, String newContent) implements ReviewCmd {}
}
