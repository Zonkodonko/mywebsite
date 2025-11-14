package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.BlogArticle;
import com.zonkodonko.ba.blog.data.BlogArticleRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * Implementation of {@link BlogArticleService}.
 *
 * @author Timm
 * @version 14.11.2025
 */
@Component
public class BlogArticleServiceImpl implements BlogArticleService {

	final BlogArticleRepository blogArticleRepository;

	public BlogArticleServiceImpl(BlogArticleRepository blogArticleRepository) {
		this.blogArticleRepository = blogArticleRepository;
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
}
