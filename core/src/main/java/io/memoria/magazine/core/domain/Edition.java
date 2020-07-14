package io.memoria.magazine.core.domain;

import io.vavr.collection.Set;

public record Edition(String name, EditionStatus editionStatus, Set<Topic>topics) {}
