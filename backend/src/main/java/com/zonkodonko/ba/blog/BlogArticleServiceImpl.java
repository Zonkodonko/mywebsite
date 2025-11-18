package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.images.Image;
import com.zonkodonko.ba.blog.data.images.ImageRepository;
import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.post.BlogArticleRepository;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import com.zonkodonko.ba.blog.data.topic.BlogTopicRepository;
import com.zonkodonko.ba.blog.rest.dtos.CreateArticleDto;
import com.zonkodonko.ba.blog.rest.dtos.TopicDto;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
	private final ImageRepository imageRepository;

	public BlogArticleServiceImpl(BlogArticleRepository blogArticleRepository, BlogTopicRepository blogTopicRepository, ImageRepository imageRepository) {
		this.blogArticleRepository = blogArticleRepository;
		this.blogTopicRepository = blogTopicRepository;
		this.imageRepository = imageRepository;
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
	public Long saveArticle(CreateArticleDto article, MultipartFile image) {
		Objects.requireNonNull(article);
		Image imageEntity = toImageEntity(image);
		BlogArticle articleEntity = BlogArticle.builder()
				.setTitle(article.getTitle())
				.setContent(article.getContent())
				.setTopic(article.getTopic())
				.setSettings(article.getSettings())
				.setImages(List.of(imageEntity))
				.build();
		return blogArticleRepository.save(articleEntity).getId();
	}


	@Override
	public void deleteArticle(Long id) {
		Objects.requireNonNull(id);
		blogArticleRepository.deleteById(id);
	}

	@Override
	public String saveTopic(TopicDto topic, MultipartFile image) {
		Objects.requireNonNull(topic);
		Image imageEntity = toImageEntity(image);
		BlogTopic blogTopic = BlogTopic.builder()
				.setId(topic.id())
				.setName(topic.title())
				.setDescription(topic.description())
				.setImage(imageEntity)
				.build();
		blogTopic = blogTopicRepository.save(blogTopic);
		return blogTopicRepository.save(blogTopic).getId();
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

	/**
	 * Save image to database.
	 * @param image image to save
	 * @return image entity
	 */
	private Image toImageEntity(MultipartFile image) {
		byte[] imgData = getImageData(image);
		Image imageEntity =
				Image.builder()
						.setContentType(image.getContentType())
						.setFilename(image.getOriginalFilename())
						.setData(imgData)
						.build();
		imageEntity = imageRepository.save(imageEntity);
		return imageEntity;
	}

	/**
	 * Get image data from multipart file. And check if the file is valid.
	 * @param image image to get data from
	 * @return image data
	 */
	private static byte[] getImageData(MultipartFile image) {
		if(image != null){
			if(image.isEmpty()){
				throw new IllegalArgumentException("Empty image");
			}
		}
		MediaType.parseMediaType(image.getContentType());//trying if the media type is valid
		byte[] imgData;
		try {
			imgData = image.getBytes();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return imgData;
	}
}
