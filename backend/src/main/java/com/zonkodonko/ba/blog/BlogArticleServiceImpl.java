package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.post.BlogArticleRepository;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import com.zonkodonko.ba.blog.data.topic.BlogTopicRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link BlogService}.
 *
 * @author Timm
 * @version 14.11.2025
 */
@Component
public class BlogArticleServiceImpl implements BlogService {

	final BlogArticleRepository blogArticleRepository;

	final BlogTopicRepository blogTopicRepository;

	public BlogArticleServiceImpl(BlogArticleRepository blogArticleRepository, BlogTopicRepository blogTopicRepository) {
		this.blogArticleRepository = blogArticleRepository;
		this.blogTopicRepository = blogTopicRepository;
	}

	@Override
	public List<BlogArticle> getArticles(String topic) {
		if (topic == null || topic.isBlank()) {
			return blogArticleRepository.findAllByOrderByCreatedDateDesc();
		}
		return blogArticleRepository.findAllByTopicOrderByCreatedDateDesc(topic);
	}

	@Override
	public BlogArticle getArticle(Long id) {
		Objects.requireNonNull(id);
		return blogArticleRepository.findById(id).orElse(null);
	}

	@Override
	public Long saveArticle(BlogArticle article) {
		Objects.requireNonNull(article);
		return blogArticleRepository.save(article).getId();
	}

	@Override
	public void deleteArticle(Long id) {
		Objects.requireNonNull(id);
		blogArticleRepository.deleteById(id);
	}

	@Override
	public String saveTopic(BlogTopic topic) {
		Objects.requireNonNull(topic);
		return blogTopicRepository.save(topic).getId();
	}

	@Override
	public Collection<BlogTopic> getTopics() {
		List<BlogTopic> topics = new ArrayList<>();
		blogTopicRepository.findAll().forEach(topics::add);
		return topics;
	}

	@Override
	public BlogTopic getTopic(String id) {
		Objects.requireNonNull(id);
		return blogTopicRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteTopic(String id) {
		deleteTopic(id, false);
	}

	@Override
	public void deleteTopic(String id, boolean withArticles) {
		blogTopicRepository.deleteById(id);
		blogArticleRepository.deleteByTopic(id);
	}
}
