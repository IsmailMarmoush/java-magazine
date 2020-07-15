package io.memoria.magazine.core.services.auth;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import static io.memoria.magazine.core.services.auth.Operation.ARTICLE_EDITING;
import static io.memoria.magazine.core.services.auth.Operation.ARTICLE_PUBLISHING;
import static io.memoria.magazine.core.services.auth.Operation.ARTICLE_SUBMISSION;
import static io.memoria.magazine.core.services.auth.Operation.EDITION_CREATION;
import static io.memoria.magazine.core.services.auth.Operation.SUGGESTION_RESOLVING;
import static io.memoria.magazine.core.services.auth.Operation.SUGGESTION_SUBMISSION;

public enum Roles {
  EDITOR_IN_CHIEF(HashSet.of(EDITION_CREATION)),
  JOURNALIST(HashSet.of(ARTICLE_SUBMISSION, ARTICLE_EDITING, ARTICLE_PUBLISHING)),
  COPYWRITER(HashSet.of(SUGGESTION_SUBMISSION, SUGGESTION_RESOLVING));

  public final Set<Operation> articleOperations;

  Roles(Set<Operation> articleOperations) {
    this.articleOperations = articleOperations;
  }
}
