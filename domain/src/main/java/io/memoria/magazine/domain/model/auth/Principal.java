package io.memoria.magazine.domain.model.auth;

import io.vavr.collection.Set;

public interface Principal {
  String id();

  Set<Role> roles();
}
