package io.memoria.magazine.core.services.dto;

import io.memoria.jutils.eventsourcing.cmd.Command;
import io.memoria.magazine.core.domain.Topic;
import io.vavr.collection.Set;

public interface EditionCmd extends Command {
  record CreateEditionDraft(String editionName, Set<Topic>topics) implements EditionCmd{}
}
