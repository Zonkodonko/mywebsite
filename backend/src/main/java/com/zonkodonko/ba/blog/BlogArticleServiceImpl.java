package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.images.Image;
import com.zonkodonko.ba.blog.data.images.ImageRepository;
import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.post.BlogArticleRepository;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import com.zonkodonko.ba.blog.data.topic.BlogTopicRepository;
import com.zonkodonko.ba.blog.rest.dtos.*;
import com.zonkodonko.ba.storage.LocalizedText;
import org.springframework.beans.factory.annotation.Value;
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

	final String hostDomain;

	public BlogArticleServiceImpl(
			BlogArticleRepository blogArticleRepository,
			BlogTopicRepository blogTopicRepository,
			ImageRepository imageRepository,
			@Value("${host.domain}") String hostDomain) {
		this.blogArticleRepository = blogArticleRepository;
		this.blogTopicRepository = blogTopicRepository;
		this.imageRepository = imageRepository;
		this.hostDomain = hostDomain;
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
				.setTitle(new LocalizedText(article.title()))
				.setContent(new LocalizedText(article.content()))
				.setTopic(article.topic())
				.setSettings(article.settings())
				.setImages(List.of(imageEntity))
				.setCreatedDate(System.currentTimeMillis())
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
	public FullBlogDto getFullBlog(String topicId) {
		BlogTopic topic = getTopic(topicId);
		List<BlogArticle> articles = blogArticleRepository.getBlogArticlesByTopic(topicId);
		return toDto(topic, articles);
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
		String image = hostDomain + "/images/topic/" + topic.getId();
		return new TopicClientDto(
				topic.getId(),
				topic.getName().getTranslations(),
				topic.getDescription().getTranslations(),
				image
		);
	}

	private ArticleClientDto toDto(BlogArticle article) {
		return new ArticleClientDto(
				article.getId(),
				article.getTitle().getTranslations(),
				article.getContent().getTranslations(),
				article.getAppearanceSettings(),
				article.getTopic()
		);
	}

	private FullBlogDto toDto(BlogTopic topic, List<BlogArticle> articles) {
		return new FullBlogDto(toDto(topic), articles.stream().map(this::toDto).toArray(ArticleClientDto[]::new));
	}
}
