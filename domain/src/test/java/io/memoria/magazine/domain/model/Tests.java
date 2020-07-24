package io.memoria.magazine.domain.model;

import io.memoria.magazine.domain.model.article.Article;
import io.memoria.magazine.domain.model.auth.Principal;
import io.memoria.magazine.domain.model.auth.Role;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import static io.memoria.magazine.domain.model.article.ArticleStatus.DRAFT;
import static io.memoria.magazine.domain.model.auth.Role.COPYWRITER;
import static io.memoria.magazine.domain.model.auth.Role.EDITOR_IN_CHIEF;
import static io.memoria.magazine.domain.model.auth.Role.JOURNALIST;

public final class Tests {
  public static record Person(String id, Set<Role>roles) implements Principal {}

  public static final Topic OOP_TOPIC = new Topic("oop");
  public static final Topic REACTIVE_TOPIC = new Topic("reactive programming");

  // Susan the chief Editor
  private static final String RAY_ID = "Ray";
  public static final Person RAY_CHIEF_EDITOR = new Person(RAY_ID, HashSet.of(EDITOR_IN_CHIEF));

  // Bob the journalist
  private static final String BOB_ID = "Bob";
  public static final Person BOB_JOURNALIST = new Person(BOB_ID, HashSet.of(JOURNALIST));
  public static final Article BOB_OOP_ARTICLE = new Article("1",
                                                            BOB_ID,
                                                            "Object Oriented Programming",
                                                            "hello world",
                                                            DRAFT,
                                                            HashSet.of(OOP_TOPIC),
                                                            HashSet.empty(),
                                                            HashSet.empty());

  // Alex the journalist
  private static final String ALEX_ID = "Alex";
  public static final Person ALEX_JOURNALIST = new Person(ALEX_ID, HashSet.of(JOURNALIST));
  public static final Article ALEX_REACTIVE_ARTICLE = new Article("2",
                                                                  ALEX_ID,
                                                                  "Reactive Programming",
                                                                  "Map reduce",
                                                                  DRAFT,
                                                                  HashSet.of(REACTIVE_TOPIC),
                                                                  HashSet.empty(),
                                                                  HashSet.empty());

  // Sam the copywriter
  private static final String SAM_ID = "Sam";
  public static final Person SAM_COPYWRITER = new Person(SAM_ID, HashSet.of(COPYWRITER));

  // Karen the copywriter
  private static final String KAREN_ID = "Karen";
  public static final Person KAREN_COPYWRITER = new Person(KAREN_ID, HashSet.of(COPYWRITER));

  private Tests() {}
}
