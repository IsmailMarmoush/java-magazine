package io.memoria.magazine.domain.model.edition;

import io.memoria.jutils.core.eventsourcing.cmd.Command;
import io.memoria.magazine.domain.model.Topic;
import io.vavr.collection.Set;

public interface EditionCmd extends Command {
  record CreateEdition(String name, Set<Topic>topics) implements EditionCmd {}
}
