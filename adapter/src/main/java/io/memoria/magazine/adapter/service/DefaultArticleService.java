//package io.memoria.magazine.adapter.service;
//
//import io.memoria.jutils.core.eventsourcing.event.EventHandler;
//import io.memoria.magazine.adapter.repo.EventRepo;
//import io.memoria.magazine.domain.model.article.Article;
//import io.memoria.magazine.domain.model.article.ArticleCmd.EditArticleTitle;
//import io.memoria.magazine.domain.model.article.ArticleCmd.SubmitDraft;
//import io.memoria.magazine.domain.model.article.ArticleEvent;
//import io.memoria.magazine.domain.model.article.ArticleEvent.DraftArticleSubmitted;
//import io.memoria.magazine.domain.model.article.ArticleEvent.ArticlePublished;
//import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;
//import io.memoria.magazine.adapter.service.ArticleService;
//import reactor.core.publisher.Mono;
//
//import java.time.LocalDateTime;
//
//public record DefaultArticleService(EventRepo<ArticleEvent>repo, EventHandler<Article, ArticleEvent>articleEventHandler)
//        implements ArticleService {
//
//  @Override
//  public Mono<DraftArticleSubmitted> create(String id, SubmitDraft submitDraft) {
//    return repo.exists(id)
//               .flatMap(exists -> (exists) ? Mono.error(new AlreadyExists())
//                                           : Mono.just(createArticle(id, submitDraft)));
//  }
//
//  @Override
//  public Mono<ArticleTitleEdited> edit(EditArticleTitle editArticleTitle) {
//    return repo.stream(editArticleTitle.id())
//               .reduce(Article.empty(), articleEventHandler)
//               .map(original -> createArticleTitleEdited(editArticleTitle));
//  }
//
//  @Override
//  public Mono<ArticlePublished> publish(String id) {
//    var articleEvents = repo.stream(id);
//    var articleMono = articleEvents.reduce(Article.empty(), articleEventHandler);
//    return articleMono.map(original -> new ArticlePublished(id, LocalDateTime.now()));
//  }
//
//  private DraftArticleSubmitted createArticle(String id, SubmitDraft submitDraft) {
//    return new DraftArticleSubmitted(id, submitDraft.title(), submitDraft.content(), LocalDateTime.now());
//  }
//
//  private ArticleTitleEdited createArticleTitleEdited(EditArticleTitle editArticleTitle) {
//    return new ArticleTitleEdited(editArticleTitle.id(), editArticleTitle.newTitle(), LocalDateTime.now());
//  }
//}
