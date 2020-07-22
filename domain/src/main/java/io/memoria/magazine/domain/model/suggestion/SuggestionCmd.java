package io.memoria.magazine.domain.model.suggestion;

import io.memoria.jutils.core.eventsourcing.cmd.Command;
import io.memoria.magazine.domain.model.auth.Principal;

public interface SuggestionCmd extends Command {

  record CreateSuggestion(Principal principal, String suggestionId, String articleId, String comment)
          implements SuggestionCmd {}

  record ResolveSuggestion(Principal principal, String suggestionId, String articleId) implements SuggestionCmd {}

  record RespondToSuggestion(Principal principal, String suggestionId, String articleId) implements SuggestionCmd {}

  String articleId();

  Principal principal();

  String suggestionId();
}
