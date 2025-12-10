package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.blog.BlogService;
import com.zonkodonko.ba.blog.ImageService;
import com.zonkodonko.ba.blog.rest.dtos.incoming.CreateArticleDto;
import com.zonkodonko.ba.blog.rest.dtos.outgoing.ArticleClientDto;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * todo write comment
 *
 * @author Timm
 * @version 10.12.2025
 */
@RestController
@RequestMapping("/blog/article")
public class ArticleController {

	private final BlogService blogArticleService;
	private final ImageService imageService;


	ArticleController(BlogService blogArticleService, ImageService imageService) {
		this.blogArticleService = blogArticleService;
		this.imageService = imageService;
	}

	@GetMapping("/{id}")
	public ArticleClientDto getArticle(@PathVariable Long id) {
		return blogArticleService.getArticle(id);
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Long saveArticle(
			@RequestPart("article") @Valid CreateArticleDto article,
			@RequestPart(name = "images", required = false) List<MultipartFile> image) throws IOException {
		return blogArticleService.saveArticle(article, image);
	}

	@PutMapping(path= "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

	@DeleteMapping("/{id}")
	public void deleteArticle(@PathVariable Long id) {
		blogArticleService.deleteArticle(id);
	}




}
