package io.memoria.magazine.domain.services.auth;

import io.vavr.collection.Set;

public interface Principal {
  String id();

  Set<Role> roles();
}
