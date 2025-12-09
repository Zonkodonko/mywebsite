package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.blog.BlogService;
import com.zonkodonko.ba.blog.ImageService;
import com.zonkodonko.ba.blog.rest.dtos.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/blog")
class BlogController {

	private final BlogService blogArticleService;
	private final ImageService imageService;


	BlogController(BlogService blogArticleService, ImageService imageService) {
		this.blogArticleService = blogArticleService;
		this.imageService = imageService;
	}

	@GetMapping("/{topic}/articles")
	public Iterable<ArticleClientDto> getArticles(@PathVariable String topic) {
		return blogArticleService.getArticles(topic);
	}

	@PostMapping(path = "/article",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Long saveArticle(
			@RequestPart("article") CreateArticleDto article,
			@RequestPart(name = "images", required = false) List<MultipartFile> image) throws IOException {
		return blogArticleService.saveArticle(article, image);
	}

	@PutMapping(path= "/article/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void updateArticle(@PathVariable Long id,
	                          @RequestPart("article") CreateArticleDto article,
							  @RequestPart(name = "imagesToDelete",required = false) Collection<String> imagesToDelete,
	                          @RequestPart(name = "images", required = false) Collection<MultipartFile> images) {
		if(imagesToDelete != null) {
			for(String imageToDelete : imagesToDelete) {
				imageService.deleteForEntity(id, imageToDelete);
			}
		}
		blogArticleService.updateArticle(id, article,images);
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
