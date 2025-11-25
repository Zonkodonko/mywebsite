package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.images.Image;
import com.zonkodonko.ba.blog.data.images.ImageRepository;
import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.post.BlogArticleRepository;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import com.zonkodonko.ba.blog.data.topic.BlogTopicRepository;
import com.zonkodonko.ba.blog.rest.dtos.ArticleClientDto;
import com.zonkodonko.ba.blog.rest.dtos.CreateArticleDto;
import com.zonkodonko.ba.blog.rest.dtos.TopicClientDto;
import com.zonkodonko.ba.blog.rest.dtos.TopicDto;
import com.zonkodonko.ba.storage.LocalizedText;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

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

	@Transactional
	@Override
	public List<ArticleClientDto> getArticles(String topic) {
		Collection<BlogArticle> articles;
		if (topic == null || topic.isBlank()) {
			articles = blogArticleRepository.findAllByOrderByCreatedDateDesc();
		} else {
			articles = blogArticleRepository.findAllByTopicOrderByCreatedDateDesc(topic);
		}
		return articles.stream().map(this::toDto).toList();
	}

	@Transactional
	@Override
	public BlogArticle getArticle(Long id) {
		Objects.requireNonNull(id);
		return blogArticleRepository.findById(id).orElse(null);
	}

	@Transactional
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

	@Transactional
	@Override
	public String saveTopic(TopicDto topic, MultipartFile image) {
		Objects.requireNonNull(topic);
		if(topic.id() != null && !topic.id().isBlank()) {
			Image oldImg = blogTopicRepository.findById(topic.id()).orElseThrow().getImage();
			imageRepository.delete(oldImg);
		}
		Image imageEntity = toImageEntity(image);
		BlogTopic blogTopic = BlogTopic.builder()
				.setId(topic.id().toLowerCase())
				.setName(new LocalizedText(topic.title()))
				.setDescription(new LocalizedText(topic.description()))
				.setImage(imageEntity)
				.build();
		blogTopic = blogTopicRepository.save(blogTopic);
		return blogTopic.getId();
	}

	@Transactional
	@Override
	public Collection<TopicClientDto> getTopics() {
		try {
			List<BlogTopic> topics = new ArrayList<>();
			blogTopicRepository.findAll().forEach(topics::add);
			return topics.stream().map(this::toDto).toList();
		} catch (RuntimeException e) {
			throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
	}

	@Transactional
	@Override
	public BlogTopic getTopic(String id) {
		Objects.requireNonNull(id);
		try {
			return blogTopicRepository.findById(id.toLowerCase()).orElse(null);
		} catch (RuntimeException e) {
			throw new ErrorResponseException(HttpStatus.INTERNAL_SERVER_ERROR, e);
		}
	}

	@Transactional
	@Override
	public void deleteTopic(String id) {
		deleteTopic(id.toLowerCase(), false);
	}

	@Override
	public void deleteTopic(String id, boolean withArticles) {
		blogTopicRepository.deleteById(id);
		blogArticleRepository.deleteByTopic(id);
	}

	/**
	 * Save image to database.
	 *
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
	 *
	 * @param image image to get data from
	 * @return image data
	 */
	private static byte[] getImageData(MultipartFile image) {
		if (image != null) {
			if (image.isEmpty()) {
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


	private TopicClientDto toDto(BlogTopic topic) {
		String base64 = Base64.getEncoder().encodeToString(topic.getImage().getData());
		return new TopicClientDto(
				topic.getId(),
				topic.getName().getTranslations(),
				topic.getDescription().getTranslations(),
				base64
		);
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
}
