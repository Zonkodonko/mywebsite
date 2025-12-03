package com.zonkodonko.ba.blog.data.topic;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 * Repository for {@link BlogTopic}.
 */
public interface BlogTopicRepository extends CrudRepository<BlogTopic, String> {

	/**
	 * Delete a topic by id with articles associated with it.
	 * @param entity
	 */

	@Query("DELETE FROM Image i WHERE i.relatedEntity IN (SELECT a.id FROM BlogArticle a WHERE a.topic = ?1)")
	void deleteRelatedImages(String topicId);

	@Query("DELETE FROM BlogArticle a WHERE a.topic = ?1")
	void deleteRelatedArticles(String topicId);

	@Override
	default void delete(BlogTopic entity) {
		deleteRelatedImages(entity.getId());
		deleteRelatedArticles(entity.getId());
		deleteById(entity.getId());
	}
}
