package io.memoria.magazine.core.services.dto;

import io.memoria.jutils.eventsourcing.cmd.Command;

public interface ArticleCmd extends Command {
  record CreateArticleDraft(String title, String content) implements ArticleCmd {}

  record EditArticleTitle(String id, String newTitle) implements ArticleCmd {}
}
