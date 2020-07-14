package io.memoria.magazine.core.services;

import io.memoria.magazine.core.services.dto.EditionEvent.EditionCreated;
import io.memoria.magazine.core.services.dto.EditionEvent.EditionPublished;
import io.memoria.magazine.core.services.dto.EditionCmd.CreateEditionDraft;
import reactor.core.publisher.Mono;

public interface EditionService {

  Mono<EditionCreated> create(CreateEditionDraft createEditionCmd);

  Mono<EditionPublished> publish(String id);
}
