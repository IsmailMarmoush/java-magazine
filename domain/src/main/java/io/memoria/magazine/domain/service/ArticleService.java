package io.memoria.magazine.domain.service;

import io.memoria.magazine.domain.model.article.ArticleEvent;
import io.memoria.magazine.domain.service.ArticleServiceDTO.EditArticleTitleDTO;
import io.memoria.magazine.domain.service.ArticleServiceDTO.PublishArticleDTO;
import io.memoria.magazine.domain.service.ArticleServiceDTO.SubmitDraftDTO;
import io.vavr.collection.List;
import reactor.core.publisher.Mono;

public interface ArticleService {

  Mono<List<ArticleEvent>> submitDraft(SubmitDraftDTO submitDraftDTO);

  Mono<List<ArticleEvent>> edit(EditArticleTitleDTO editArticleTitleDTO);

  Mono<List<ArticleEvent>> publish(PublishArticleDTO publishArticleDTO);
}
