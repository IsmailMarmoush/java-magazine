package io.memoria.magazine.core.services.dto;

import io.memoria.jutils.eventsourcing.cmd.Command;

public interface ReviewCmd extends Command {

  record CreateContentReview(String articleId, String newContent) implements ReviewCmd {}
}
