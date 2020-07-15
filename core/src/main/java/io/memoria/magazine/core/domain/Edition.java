package io.memoria.magazine.core.domain;

import io.vavr.collection.HashSet;
import io.vavr.collection.Set;

public record Edition(String name, EditionStatus editionStatus, Set<Topic>topics) {
  public static Edition empty() {
    return new Edition("", EditionStatus.DRAFT, HashSet.empty());
  }

  public Edition addTopic(Topic topic) {
    return new Edition(this.name, this.editionStatus, this.topics.add(topic));
  }

  public Edition toPublished() {
    return new Edition(this.name, EditionStatus.PUBLISHED, this.topics);
  }

  public Edition withName(String name) {
    return new Edition(name, this.editionStatus, this.topics);
  }

  public Edition withTopics(Set<Topic> topics) {
    return new Edition(this.name, this.editionStatus, topics);
  }
}
