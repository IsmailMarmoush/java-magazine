package io.memoria.magazine.adapter.eventhandler;

import io.memoria.jutils.eventsourcing.event.EventHandler;
import io.memoria.magazine.core.domain.Edition;
import io.memoria.magazine.core.domain.EditionStatus;
import io.memoria.magazine.core.services.dto.EditionEvent;
import io.memoria.magazine.core.services.dto.EditionEvent.EditionCreated;
import io.memoria.magazine.core.services.dto.EditionEvent.EditionPublished;

public record EditionEventHandler() implements EventHandler<Edition, EditionEvent> {
  @Override
  public Edition apply(Edition edition, EditionEvent editionEvent) {
    if (editionEvent instanceof EditionCreated e) {
      return new Edition(e.editionName(), EditionStatus.DRAFT, e.topics());
    } else if (editionEvent instanceof EditionPublished e) {
      return new Edition(edition.name(), EditionStatus.PUBLISHED, edition.topics());
    }
    throw new UnsupportedOperationException();
  }
}
