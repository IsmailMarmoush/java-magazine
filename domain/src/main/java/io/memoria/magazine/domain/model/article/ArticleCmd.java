package io.memoria.magazine.domain.model.article;

import io.memoria.jutils.core.eventsourcing.cmd.Command;
import io.memoria.magazine.domain.model.Topic;
import io.memoria.magazine.domain.services.auth.Principal;
import io.vavr.collection.Set;

public interface ArticleCmd extends Command {
  record EditArticleTitle(Principal principal, String articleId, String newTitle) implements ArticleCmd {}

  record PublishArticle(Principal principal, String articleId) implements ArticleCmd {}

  record SubmitDraft(Principal principal, String articleId, String title, String content, Set<Topic>topics)
          implements ArticleCmd {}

  Principal principal();
}
