package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.blog.BlogArticleService;
import com.zonkodonko.ba.blog.data.BlogArticle;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/blog")
class BlogController {

	private final BlogArticleService blogArticleService;


	BlogController(BlogArticleService blogArticleService) {
		this.blogArticleService = blogArticleService;
	}

	@GetMapping("/{topic}/articles")
	public Iterable<BlogArticle> getArticles(@PathVariable String topic) {
		return blogArticleService.getArticles(topic);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Long saveArticle(
			@RequestPart("article") ArticleDto article,
			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

		BlogArticle fullArticle = BlogArticle.builder()
				.setId(article.id())
				.setTitle(article.title())
				.setContent(article.content())
				.setCreatedDate(article.createdDate())
				.setImage(image.getBytes())
				.setSettings(article.settings())
				.setTopic(article.topic())
				.build();

		return blogArticleService.saveArticle(fullArticle);
	}

	@DeleteMapping("/{id}")
	public void deleteArticle(@PathVariable Long id) {
		blogArticleService.deleteArticle(id);
	}

}
