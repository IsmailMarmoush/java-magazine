package io.memoria.magazine.adapter.service;

import io.memoria.magazine.adapter.eventhandler.EditionEventHandler;
import io.memoria.magazine.adapter.repo.EditionEventRepo;
import io.memoria.magazine.core.domain.Edition;
import io.memoria.magazine.core.services.EditionService;
import io.memoria.magazine.core.services.dto.EditionCmd.CreateEditionDraft;
import io.memoria.magazine.core.services.dto.EditionEvent.EditionCreated;
import io.memoria.magazine.core.services.dto.EditionEvent.EditionPublished;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public record DefaultEditionService(EditionEventRepo repo, EditionEventHandler edititionEventHandler)
        implements EditionService {
  @Override
  public Mono<EditionCreated> create(String id, CreateEditionDraft createEditionCmd) {
    return Mono.just(new EditionCreated(id, createEditionCmd.editionName(), createEditionCmd.topics()));
  }

  @Override
  public Mono<EditionPublished> publish(String id) {
    var editionEvents = repo.getEvents(id);
    var editionMono = editionEvents.reduce(Edition.empty(), edititionEventHandler);
    return editionMono.map(ed -> new EditionPublished(id, LocalDateTime.now()));
  }
}
