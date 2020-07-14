package io.memoria.magazine.core.services.dto;

import io.memoria.magazine.core.domain.Topic;
import io.vavr.collection.Set;

import java.time.LocalDateTime;

public interface EditionEvent {
  record EditionCreated(String editionId, String editionName, Set<Topic>topics) implements EditionEvent {}

  record EditionPublished(String editionId, LocalDateTime publishDate) implements EditionEvent {}

  String editionId();
}
