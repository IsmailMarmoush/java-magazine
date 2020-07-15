# Magazine

## User Stories

1. As an editor in chief, I add a list of topics for the new edition of a magazine.
2. As a journalist, I submit a draft article for publishing to a given topic
3. As a copywriter, I suggest changes to the draft article I'm assigned to
4. As a journalist, I respond to suggestions by making the suggested changes
5. As a copywriter, I resolve suggestions that the journalist applied
6. As a journalist, I can publish the article after all suggestions are resolved

### Details

- An article is a simple text with a title and some headings
- The draft article:
    - needs to have title and content
    - can be connected to multiple topics
- Journalists cannot change each other's drafts
- Copywriters can only suggest changes to article they were assigned to.
- Suggested changes can be considered as comments to the whole article. They should be non-empty chunks of text.
- Once the article is published, suggestions are no longer allowed.


## Notes:
* Many crud operations that are "good to have" are not implemented for the following reasons:
    * They aren't specifically mentioned in the requirements (e.g deletion of edition draft)
    * The logic doesn't fail without them, and they might not be ever needed (YAGNI)
    * Decreasing refactoring domain after initial review iterations
* Application (Api server) module isn't included as it wasn't required yet.
* Assuming there would be a diff engine which would compare contents and display them in a (github way) and its implementation is not in scope of yet 
* Assuming the following business rules:
    * A review can't be resolved till it's fulfilled first
    * An article title can't be changed if it was published
