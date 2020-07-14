package io.memoria.magazine.adapter.auth;

import io.memoria.magazine.core.services.AuthService;
import io.memoria.magazine.core.services.auth.Operation;
import reactor.core.publisher.Mono;

public record DefaultAuthService() implements AuthService {

  @Override
  public Mono<Boolean> isAuthorized(String principalId, Operation operation) {
    return null;
  }
}
