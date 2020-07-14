package io.memoria.magazine.core.services;

import io.memoria.magazine.core.services.dto.SuggestionCmd.CreateSuggestion;
import io.memoria.magazine.core.services.dto.SuggestionEvent.SuggestionCreated;
import io.memoria.magazine.core.services.dto.SuggestionEvent.SuggestionFulfilled;
import io.memoria.magazine.core.services.dto.SuggestionEvent.SuggestionResolved;
import reactor.core.publisher.Mono;

public interface SuggestionsService {

  Mono<SuggestionCreated> create(CreateSuggestion createSuggestion);

  Mono<SuggestionFulfilled> fulfill(String suggestionId);

  Mono<SuggestionResolved> resolve(String suggestionId);
}
