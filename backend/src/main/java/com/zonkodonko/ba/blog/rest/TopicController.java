package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.blog.BlogService;
import com.zonkodonko.ba.blog.ImageService;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import com.zonkodonko.ba.blog.rest.dtos.incoming.TopicDto;
import com.zonkodonko.ba.blog.rest.dtos.outgoing.ArticleClientDto;
import com.zonkodonko.ba.blog.rest.dtos.outgoing.FullBlogDto;
import com.zonkodonko.ba.blog.rest.dtos.outgoing.TopicClientDto;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("/blog/topic")
class TopicController {

	private final BlogService blogArticleService;


	TopicController(BlogService blogArticleService, ImageService imageService) {
		this.blogArticleService = blogArticleService;
	}


	@GetMapping("/all")
	public Collection<TopicClientDto> getTopics() {
		return blogArticleService.getTopics();
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public String saveTopic(
			@RequestPart("topic") TopicDto topic,
			@RequestPart(name = "image", required = false) MultipartFile image) throws IOException {

		return blogArticleService.saveTopic(topic, image);
	}

	@DeleteMapping("/{id}")
	public void deleteTopic(@PathVariable String id) {
		blogArticleService.deleteTopic(id);
	}

	@GetMapping("/{id}")
	public BlogTopic getTopic(@PathVariable String id) {
		return blogArticleService.getTopic(id);
	}

	@GetMapping("/{id}/articles")
	public FullBlogDto getTopicFull(@PathVariable String id) {
		return blogArticleService.getFullBlog(id);
	}

}
