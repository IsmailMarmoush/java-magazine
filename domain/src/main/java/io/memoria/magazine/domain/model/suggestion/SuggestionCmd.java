package io.memoria.magazine.domain.model.suggestion;

import io.memoria.jutils.core.eventsourcing.cmd.Command;

public interface SuggestionCmd extends Command {

  record CreateSuggestion(String articleId, String newContent) implements SuggestionCmd {}

  record ResolveSuggestion(String suggestionId, String articleId, String newContent) implements SuggestionCmd {}
}
