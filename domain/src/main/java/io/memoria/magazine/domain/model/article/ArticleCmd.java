package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.cmd.Command;
import io.memoria.magazine.domain.model.Topic;
import io.memoria.magazine.domain.services.auth.Principal;
import io.vavr.collection.Set;

public interface ArticleCmd extends Command {
  Principal principal();

  record SubmitDraft(Principal principal, String title, String content, Set<Topic>topics) implements ArticleCmd {}

  record EditArticleTitle(Principal principal, String id, String newTitle) implements ArticleCmd {}

  record PublishArticle(Principal principal, String id) implements ArticleCmd {}
}
