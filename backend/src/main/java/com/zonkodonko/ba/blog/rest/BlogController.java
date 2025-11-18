package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.blog.BlogService;
import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import com.zonkodonko.ba.blog.rest.dtos.ArticleClientDto;
import com.zonkodonko.ba.blog.rest.dtos.CreateArticleDto;
import com.zonkodonko.ba.blog.rest.dtos.TopicClientDto;
import com.zonkodonko.ba.blog.rest.dtos.TopicDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;

@RestController
@RequestMapping("/blog")
class BlogController {

	private final BlogService blogArticleService;


	BlogController(BlogService blogArticleService) {
		this.blogArticleService = blogArticleService;
	}

	@GetMapping("/{topic}/articles")
	public Iterable<ArticleClientDto> getArticles(@PathVariable String topic) {
		return blogArticleService.getArticles(topic).stream().map(this::toDto).toList();
	}

	@PostMapping(path = "/article",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Long saveArticle(
			@RequestPart("article") CreateArticleDto article,
			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {
		return blogArticleService.saveArticle(article, image);
	}

	@GetMapping("/topics")
	public Collection<TopicClientDto> getTopics() {
		return blogArticleService.getTopics().stream().map(this::toDto).toList();
	}

	@DeleteMapping("/article/{id}")
	public void deleteArticle(@PathVariable Long id) {
		blogArticleService.deleteArticle(id);
	}


	@PostMapping(path = "/topic",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String saveTopic(
			@RequestPart("getTopic") TopicDto topic,
			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

		return blogArticleService.saveTopic(topic, image);
	}

	@DeleteMapping("/topic/{id}")
	public void deleteTopic(@PathVariable String id) {
		blogArticleService.deleteTopic(id);
	}

	private ArticleClientDto toDto(BlogArticle article) {
		return new ArticleClientDto(
				article.getId(),
				article.getTitle(),
				article.getContent(),
				article.getPostSettings(),
				article.getTopic()
		);
	}

	private TopicClientDto toDto(BlogTopic topic) {
		String base64 = Base64.getEncoder().encodeToString(topic.getImage().getData());
		return new TopicClientDto(
				topic.getId(),
				topic.getName(),
				topic.getDescription(),
				base64
		);
	}

}
