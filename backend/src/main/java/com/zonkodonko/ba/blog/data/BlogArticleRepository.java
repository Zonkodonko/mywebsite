package com.zonkodonko.ba.blog.data;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlogArticleRepository extends CrudRepository<BlogArticle, Long> {

	List<BlogArticle> findAllByOrderByCreatedDateDesc();

	List<BlogArticle> findAllByTopicOrderByCreatedDateDesc(String topic);
}
