package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.blog.BlogService;
import com.zonkodonko.ba.blog.rest.dtos.outgoing.ArticleWithoutContent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

/**
 * Controller to handle blog requests that handle topic and articles
 *
 * @author Timm
 * @version 10.12.2025
 */
@RestController
@RequestMapping("/blog")
public class BlogController {

	private final BlogService blogArticleService;


	BlogController(BlogService blogArticleService) {
		this.blogArticleService = blogArticleService;
	}

	/**
	 * Returns all articles for given topic without content
	 *
	 * @param topic Topic name
	 * @return List of articles
	 */
	@GetMapping("/{topic}/articles")
	public Collection<ArticleWithoutContent> getArticles(@PathVariable String topic) {
		return blogArticleService.getArticlesWithoutContent(topic);
	}
}
