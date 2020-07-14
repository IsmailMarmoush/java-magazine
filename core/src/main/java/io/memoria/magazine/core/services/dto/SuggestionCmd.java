package io.memoria.magazine.core.services.dto;

import io.memoria.jutils.eventsourcing.cmd.Command;

public interface SuggestionCmd extends Command {

  record CreateContentSuggestion(String articleId, String newContent) implements SuggestionCmd {}

  record CreateTitleSuggestion(String articleId, String newTitle) implements SuggestionCmd {}
}
