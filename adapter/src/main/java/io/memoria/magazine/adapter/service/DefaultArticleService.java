package io.memoria.magazine.adapter.service;

import io.memoria.jutils.core.eventsourcing.cmd.CommandHandler;
import io.memoria.jutils.core.eventsourcing.event.EventHandler;
import io.memoria.jutils.core.generator.IdGenerator;
import io.memoria.magazine.domain.model.article.Article;
import io.memoria.magazine.domain.model.article.ArticleCmd;
import io.memoria.magazine.domain.model.article.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.domain.model.article.ArticleCmd.PublishArticle;
import io.memoria.magazine.domain.model.article.ArticleCmd.SubmitDraft;
import io.memoria.magazine.domain.model.article.ArticleEvent;
import io.memoria.magazine.domain.service.ArticleService;
import io.memoria.magazine.domain.service.ArticleServiceDTO.EditArticleTitleDTO;
import io.memoria.magazine.domain.service.ArticleServiceDTO.PublishArticleDTO;
import io.memoria.magazine.domain.service.ArticleServiceDTO.SubmitDraftDTO;
import io.memoria.magazine.domain.service.EventRepo;
import io.vavr.collection.List;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import static io.memoria.jutils.core.functional.ReactorVavrUtils.toMono;

public record DefaultArticleService(IdGenerator idgenerator,
                                    EventRepo<ArticleEvent>repo,
                                    EventHandler<Article, ArticleEvent>eventHandler,
                                    CommandHandler<Article, ArticleCmd, ArticleEvent>commandHandler)
        implements ArticleService {
  @Override
  public Mono<List<ArticleEvent>> submitDraft(SubmitDraftDTO dto) {
    String id = idgenerator.get();
    var submitDraftCmd = new SubmitDraft(dto.principal(), id, dto.title(), dto.content(), dto.topics());
    var tryToSubmitDraft = commandHandler.apply(Article.empty(), submitDraftCmd);
    return toMono(tryToSubmitDraft).zipWhen(events -> repo.add(id, events)).map(Tuple2::getT1);
  }

  @Override
  public Mono<List<ArticleEvent>> edit(EditArticleTitleDTO dto) {
    var editArticleTitleCmd = new EditArticleTitle(dto.principal(), dto.articleId(), dto.newTitle());
    var articleEvents = repo.stream(dto.articleId());
    var articleMono = articleEvents.reduce(Article.empty(), eventHandler);
    return articleMono.flatMap(article -> toMono(commandHandler.apply(article, editArticleTitleCmd)))
                      .zipWhen(events -> repo.add(dto.articleId(), events))
                      .map(Tuple2::getT1);
  }

  @Override
  public Mono<List<ArticleEvent>> publish(PublishArticleDTO dto) {
    var publishArticleCmd = new PublishArticle(dto.principal(), dto.articleId());
    var articleEvents = repo.stream(dto.articleId());
    var articleMono = articleEvents.reduce(Article.empty(), eventHandler);
    return articleMono.flatMap(article -> toMono(commandHandler.apply(article, publishArticleCmd)))
                      .zipWhen(events -> repo.add(dto.articleId(), events))
                      .map(Tuple2::getT1);
  }
}
