package io.memoria.magazine.adapter.service;

import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.CreateSuggestion;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionCreated;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionFulfilled;
import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionResolved;
import reactor.core.publisher.Mono;

public interface SuggestionService {

  Mono<SuggestionCreated> create(String id, CreateSuggestion createSuggestion);

  Mono<SuggestionFulfilled> fulfill(String suggestionId);

  Mono<SuggestionResolved> resolve(String suggestionId);
}
