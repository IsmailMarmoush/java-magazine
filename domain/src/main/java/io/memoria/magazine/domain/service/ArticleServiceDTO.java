package io.memoria.magazine.domain.service;

import io.memoria.magazine.domain.model.Topic;
import io.memoria.magazine.domain.model.auth.Principal;
import io.vavr.collection.Set;

public interface ArticleServiceDTO {
  record EditArticleTitleDTO(Principal principal, String articleId, String newTitle) implements ArticleServiceDTO {}

  record PublishArticleDTO(Principal principal, String articleId) implements ArticleServiceDTO {}

  record SubmitDraftDTO(Principal principal, String title, String content, Set<Topic>topics)
          implements ArticleServiceDTO {}

  Principal principal();
}
