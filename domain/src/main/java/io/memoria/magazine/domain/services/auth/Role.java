package io.memoria.magazine.domain.services.auth;

import io.memoria.jutils.core.eventsourcing.cmd.Command;
import io.memoria.magazine.domain.model.article.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.domain.model.article.ArticleCmd.PublishArticle;
import io.memoria.magazine.domain.model.article.ArticleCmd.SubmitDraft;
import io.memoria.magazine.domain.model.edition.EditionCmd.CreateEdition;
import io.memoria.magazine.domain.model.review.ReviewCmd.CreateReview;
import io.memoria.magazine.domain.model.review.ReviewCmd.ResolveReview;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

public enum Role {
  EDITOR_IN_CHIEF(HashSet.of(CreateEdition.class)),
  JOURNALIST(HashSet.of(SubmitDraft.class, EditArticleTitle.class, PublishArticle.class)),
  COPYWRITER(HashSet.of(CreateReview.class, ResolveReview.class));

  public final Set<Class<? extends Command>> operations;

  Role(Set<Class<? extends Command>> operations) {
    this.operations = operations;
  }

  public static <T extends Command> boolean isAbleTo(Set<Role> roles, Class<T> t) {
    return roles.filter(role -> role.operations.contains(t)).size() > 0;
  }
}
