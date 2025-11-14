package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.blog.BlogService;
import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/blog")
class BlogController {

	record TopicDto(String id, String name, String description) {
	}

	private final BlogService blogArticleService;


	BlogController(BlogService blogArticleService) {
		this.blogArticleService = blogArticleService;
	}

	@GetMapping("/{topic}/articles")
	public Iterable<BlogArticle> getArticles(@PathVariable String topic) {
		return blogArticleService.getArticles(topic);
	}

	@PostMapping(path = "/article",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

	@GetMapping("/topics")
	public Collection<BlogTopic> getTopics() {
		return blogArticleService.getTopics();
	}

	@DeleteMapping("/article/{id}")
	public void deleteArticle(@PathVariable Long id) {
		blogArticleService.deleteArticle(id);
	}


	@PostMapping(path = "/topic",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String saveTopic(
			@RequestPart("topic") TopicDto topic,
			@RequestPart(value = "image", required = false) MultipartFile image) throws IOException {

		BlogTopic fullTopic = BlogTopic.builder()
				.setId(topic.id())
				.setName(topic.name())
				.setDescription(topic.description())
				.setImage(image != null ? image.getBytes() : null)
				.build();

		return blogArticleService.saveTopic(fullTopic);
	}

	@DeleteMapping("/topic/{id}")
	public void deleteTopic(@PathVariable String id) {
		blogArticleService.deleteTopic(id);
	}

}
