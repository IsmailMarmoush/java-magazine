package io.memoria.magazine.adapter.service;

import io.memoria.magazine.adapter.repo.AuthRepo;
import io.memoria.magazine.domain.services.AuthService;
import io.memoria.magazine.domain.services.auth.Operation;
import reactor.core.publisher.Mono;

public record DefaultAuthService(AuthRepo repo) implements AuthService {

  @Override
  public Mono<Boolean> isAuthorized(String principalId, Operation operation) {
    return repo.isAuthorized(principalId, operation);
  }
}
