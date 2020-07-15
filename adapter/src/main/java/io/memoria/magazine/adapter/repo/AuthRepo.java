package io.memoria.magazine.adapter.repo;

import io.memoria.magazine.core.services.auth.Operation;
import reactor.core.publisher.Mono;

public interface AuthRepo {
  Mono<Boolean> isAuthorized(String principalId, Operation operation);
}
