package com.zonkodonko.ba.blog.data.post;

import com.zonkodonko.ba.blog.data.images.Image;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlogArticleRepository extends CrudRepository<BlogArticle, Long> {

	List<BlogArticle> findAllByOrderByCreatedDateDesc();

	List<BlogArticle> findAllByTopicOrderByCreatedDateDesc(String topic);

	void deleteByTopic(String topic);

	List<BlogArticle> getBlogArticlesByTopic(String topic);

	@Query("DELETE FROM Image i WHERE i.relatedEntity = ?1")
	void deleteRelatedImages(Long articleId);

	@Override
	default void delete(BlogArticle entity) {
		deleteRelatedImages(entity.getId());
		deleteById(entity.getId());
	}
}
