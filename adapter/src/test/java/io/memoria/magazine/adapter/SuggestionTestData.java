//package io.memoria.magazine.adapter;
//
//import io.memoria.magazine.domain.model.suggestion.SuggestionCmd.CreateContentSuggestion;
//import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionCreated;
//import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionFulfilled;
//import io.memoria.magazine.domain.model.suggestion.SuggestionEvent.SuggestionResolved;
//
//import static io.memoria.magazine.adapter.ArticleTestData.ARTICLE_ID;
//
//public class SuggestionTestData {
//  static final String REVIEW_ID = "suggestion_01";
//  static final String SUGGESTED_CONTENT = "Reactive Programming CS 101";
//  static final SuggestionCreated CONTENT_REVIEW_CREATED = new SuggestionCreated(REVIEW_ID,
//                                                                        ARTICLE_ID,
//                                                                        SUGGESTED_CONTENT);
//  static final SuggestionFulfilled REVIEW_FULFILLED = new SuggestionFulfilled(REVIEW_ID);
//  static final SuggestionResolved REVIEW_RESOLVED = new SuggestionResolved(REVIEW_ID);
//
//  static final CreateContentSuggestion CREATE_CONTENT_REVIEW = new CreateContentSuggestion(ARTICLE_ID, SUGGESTED_CONTENT);
//
//  private SuggestionTestData() {}
//}
