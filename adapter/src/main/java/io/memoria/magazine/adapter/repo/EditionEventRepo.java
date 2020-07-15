package io.memoria.magazine.adapter.repo;

import io.memoria.magazine.core.services.dto.EditionEvent;
import reactor.core.publisher.Flux;

public interface EditionEventRepo {
  Flux<EditionEvent> getEvents(String id);
}
