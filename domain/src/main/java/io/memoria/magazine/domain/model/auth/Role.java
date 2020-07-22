package io.memoria.magazine.domain.model.auth;

import io.memoria.jutils.core.eventsourcing.cmd.Command;
import io.memoria.magazine.domain.model.article.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.domain.model.article.ArticleCmd.PublishArticle;
import io.memoria.magazine.domain.model.article.ArticleCmd.SubmitDraft;
import io.memoria.magazine.domain.model.edition.EditionCmd.CreateEdition;
import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.CreateSuggestion;
import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.ResolveSuggestion;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

public enum Role {
  EDITOR_IN_CHIEF(HashSet.of(CreateEdition.class)),
  JOURNALIST(HashSet.of(SubmitDraft.class, EditArticleTitle.class, PublishArticle.class)),
  COPYWRITER(HashSet.of(CreateSuggestion.class, ResolveSuggestion.class));

  public final Set<Class<? extends Command>> operations;

  public static <T extends Command> boolean isAbleTo(Set<Role> roles, Class<T> t) {
    return roles.filter(role -> role.operations.contains(t)).size() > 0;
  }

  Role(Set<Class<? extends Command>> operations) {
    this.operations = operations;
  }
}
