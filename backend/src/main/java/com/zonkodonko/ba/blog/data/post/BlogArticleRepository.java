package com.zonkodonko.ba.blog.data.post;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlogArticleRepository extends CrudRepository<BlogArticle, Long> {

	List<BlogArticle> findAllByOrderByLastChangeDesc();

	List<BlogArticle> findAllByTopicOrderByCreatedDesc(String topic);

	void deleteByTopic(String topic);

	List<BlogArticle> getBlogArticlesByTopic(String topic);

	@Modifying
	@Query("DELETE FROM Image i WHERE i.relatedEntity = ?1")
	void deleteRelatedImages(Long articleId);

	@Override
	default void delete(BlogArticle entity) {
		deleteRelatedImages(entity.getId());
		deleteById(entity.getId());
	}

	/**
	 * Deletes article and all related images.
	 * @param id Article id
	 */
	default void deleteWithImages(Long id) {
		deleteRelatedImages(id);
		deleteById(id);
	}
}
