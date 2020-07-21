package io.memoria.magazine.domain.model.review;

import io.memoria.jutils.core.eventsourcing.cmd.Command;

public interface ReviewCmd extends Command {

  record CreateContentReview(String articleId, String newContent) implements ReviewCmd {}
}
