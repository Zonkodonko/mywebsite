package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.blog.BlogService;
import com.zonkodonko.ba.blog.rest.dtos.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
		return blogArticleService.getArticles(topic);
	}

	@PostMapping(path = "/article",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Long saveArticle(
			@RequestPart("article") CreateArticleDto article,
			@RequestPart(name = "image", required = false) MultipartFile image) throws IOException {
		return blogArticleService.saveArticle(article, image);
	}

	@PutMapping("/article/{id}")
	public void updateArticle(@PathVariable Long id,
	                          @RequestPart("article") CreateArticleDto article,
	                          @RequestPart(name = "image", required = false) MultipartFile image) {
		blogArticleService.updateArticle(id, article,image);
	}

	@GetMapping("/topics")
	public Collection<TopicClientDto> getTopics() {
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
			@RequestPart(name = "image", required = false) MultipartFile image) throws IOException {

		return blogArticleService.saveTopic(topic, image);
	}

	@DeleteMapping("/topic/{id}")
	public void deleteTopic(@PathVariable String id) {
		blogArticleService.deleteTopic(id);
	}

	@GetMapping("/topic/{id}")
	public FullBlogDto getTopic(@PathVariable String id) {
		return blogArticleService.getFullBlog(id);
	}

}
