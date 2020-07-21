package io.memoria.magazine.adapter;

import io.memoria.magazine.domain.model.article.ArticleCmd.SubmitDraft;
import io.memoria.magazine.domain.model.article.ArticleCmd.EditArticleTitle;
import io.memoria.magazine.domain.model.article.ArticleEvent;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleCreated;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticlePublished;
import io.memoria.magazine.domain.model.article.ArticleEvent.ArticleTitleEdited;

import java.time.LocalDateTime;

public class ArticleTestData {
  //***************************************************************************
  // Events
  //***************************************************************************
  // New article
  static final String ARTICLE_ID = "article_01";
  static final String ARTICLE_TITLE = "Reactive Programming";
  static final String ARTICLE_CONTENT = "Hello world";
  static final LocalDateTime CREATION_DATE = LocalDateTime.of(2021, 1, 1, 1, 0);
  static final ArticleCreated ARTICLE_CREATED = new ArticleCreated(ARTICLE_ID,
                                                                   ARTICLE_TITLE,
                                                                   ARTICLE_CONTENT,
                                                                   CREATION_DATE);

  // Edited title
  static final LocalDateTime EDIT_DATE = LocalDateTime.of(2022, 2, 2, 2, 0);
  static final String NEW_TITLE = "Reactive Programming 101";
  static final ArticleTitleEdited TITLE_EDITED = new ArticleTitleEdited(ArticleTestData.ARTICLE_ID,
                                                                        NEW_TITLE,
                                                                        EDIT_DATE);

  // Published
  static final LocalDateTime PUBLISH_DATE = LocalDateTime.of(2023, 3, 3, 3, 0);
  static final ArticlePublished ARTICLE_PUBLISHED = new ArticleEvent.ArticlePublished(ARTICLE_ID, PUBLISH_DATE);

  //***************************************************************************
  // Commands
  //***************************************************************************
  static final SubmitDraft CREATE_ARTICLE_DRAFT = new SubmitDraft(ARTICLE_TITLE, ARTICLE_CONTENT);
  static final EditArticleTitle EDIT_ARTICLE_TITLE = new EditArticleTitle(ARTICLE_ID, NEW_TITLE);

  private ArticleTestData() {}
}
