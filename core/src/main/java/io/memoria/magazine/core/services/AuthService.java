package io.memoria.magazine.core.services;

import io.memoria.magazine.core.services.auth.Operation;
import reactor.core.publisher.Mono;

public interface AuthService {
  Mono<Boolean> isAuthorized(String principalId, Operation operation);
}
