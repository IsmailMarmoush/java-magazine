package io.memoria.magazine.core.services.dto;

import io.memoria.jutils.eventsourcing.cmd.Command;

public interface SuggestionCmd extends Command {

  record CreateSuggestion(String articleId, String newTitle, String newContent) implements SuggestionCmd{}
}
