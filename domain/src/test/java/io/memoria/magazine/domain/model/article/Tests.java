package io.memoria.magazine.domain.model.article;

import io.memoria.magazine.domain.model.Topic;
import io.memoria.magazine.domain.services.auth.Principal;
import io.memoria.magazine.domain.services.auth.Role;
import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

import static io.memoria.magazine.domain.model.article.ArticleStatus.DRAFT;
import static io.memoria.magazine.domain.services.auth.Role.EDITOR_IN_CHIEF;
import static io.memoria.magazine.domain.services.auth.Role.JOURNALIST;

public final class Tests {
  public static record Person(String id, Set<Role>roles) implements Principal {}

  public static final Topic OOP_TOPIC;
  public static final Topic REACTIVE_TOPIC;

  public static final String BOB_ID;
  public static final Person BOB_JOURNALIST;
  public static final Article BOB_OOP_ARTICLE;

  public static final String ALEX_ID;
  public static final Person ALEX_JOURNALIST;
  public static final Article ALEX_REACTIVE_ARTICLE;

  public static final String SUSAN_ID;
  public static final Person SUSAN_EDITOR;

  static {
    OOP_TOPIC = new Topic("oop");
    REACTIVE_TOPIC = new Topic("reactive programming");

    // Bob
    BOB_ID = "Bob";
    BOB_JOURNALIST = new Person(BOB_ID, HashSet.of(JOURNALIST));
    BOB_OOP_ARTICLE = new Article("1",
                                  BOB_ID,
                                  "Object Oriented Programming",
                                  "hello world",
                                  DRAFT,
                                  HashSet.of(OOP_TOPIC));

    // Alex
    ALEX_ID = "Alex";
    ALEX_JOURNALIST = new Person(ALEX_ID, HashSet.of(JOURNALIST));
    ALEX_REACTIVE_ARTICLE = new Article("2",
                                        ALEX_ID,
                                        "Reactive Programming",
                                        "Map reduce",
                                        DRAFT,
                                        HashSet.of(REACTIVE_TOPIC));
    // SUSAN
    SUSAN_ID = "Susan";
    SUSAN_EDITOR = new Person(SUSAN_ID, HashSet.of(EDITOR_IN_CHIEF));

  }

  private Tests() {}
}
