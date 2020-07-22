//package io.memoria.magazine.adapter.service;
//
//import io.memoria.jutils.eventsourcing.event.EventHandler;
//import io.memoria.magazine.adapter.repo.EventRepo;
//import io.memoria.magazine.domain.model.suggestion.Suggestion;
//import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.CreateContentSuggestion;
//import io.memoria.magazine.domain.model.suggestion.SuggestionEvent;
//import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionCreated;
//import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionFulfilled;
//import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionResolved;
//import io.memoria.magazine.domain.services.SuggestionService;
//import reactor.core.publisher.Mono;
//
//public record DefaultSuggestionService(EventRepo<SuggestionEvent>repo, EventHandler<Suggestion, SuggestionEvent>eventHandler)
//        implements SuggestionService {
//  @Override
//  public Mono<SuggestionCreated> create(String id, CreateContentSuggestion s) {
//    return repo.exists(id)
//               .flatMap(exists -> (exists) ? Mono.error(new AlreadyExists()) : Mono.just(createSuggestion(id, s)));
//  }
//
//  @Override
//  public Mono<SuggestionFulfilled> fulfill(String suggestionId) {
//    return repo.stream(suggestionId).reduce(Suggestion.empty(), eventHandler).map(suggestion -> new SuggestionFulfilled(suggestionId));
//  }
//
//  @Override
//  public Mono<SuggestionResolved> resolve(String suggestionId) {
//    return repo.stream(suggestionId).reduce(Suggestion.empty(), eventHandler).map(suggestion -> new SuggestionResolved(suggestionId));
//  }
//
//  private SuggestionCreated createSuggestion(String id, CreateContentSuggestion s) {
//    return new SuggestionCreated(id, s.articleId(), s.newContent());
//  }
//}
