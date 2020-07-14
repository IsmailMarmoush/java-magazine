package io.memoria.magazine.core.services.dto;

import io.memoria.jutils.eventsourcing.cmd.Command;

public interface ArticleCmd extends Command {
  record CreateArticleDraft(String title, String content) implements ArticleCmd{}

  record EditArticle(String id, String newTitle, String newContent) implements ArticleCmd{}
}
