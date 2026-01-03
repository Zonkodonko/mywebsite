package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.images.Image;
import com.zonkodonko.ba.blog.data.images.ImageRepository;
import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.post.BlogArticleRepository;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import com.zonkodonko.ba.blog.data.topic.BlogTopicRepository;
import com.zonkodonko.ba.blog.rest.dtos.incoming.CreateArticleDto;
import com.zonkodonko.ba.blog.rest.dtos.incoming.TopicDto;
import com.zonkodonko.ba.blog.rest.dtos.outgoing.ArticleClientDto;
import com.zonkodonko.ba.blog.rest.dtos.outgoing.ArticleWithoutContent;
import com.zonkodonko.ba.blog.rest.dtos.outgoing.FullBlogDto;
import com.zonkodonko.ba.blog.rest.dtos.outgoing.TopicClientDto;
import com.zonkodonko.ba.storage.LocalizedText;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;
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
public class BlogServiceImpl implements BlogService {

	final BlogArticleRepository blogArticleRepository;

	final BlogTopicRepository blogTopicRepository;
	private final ImageRepository imageRepository;

	final String hostDomain;

	public BlogServiceImpl(
			BlogArticleRepository blogArticleRepository,
			BlogTopicRepository blogTopicRepository,
			ImageRepository imageRepository,
			@Value("${server.domain}") String hostDomain) {
		this.blogArticleRepository = blogArticleRepository;
		this.blogTopicRepository = blogTopicRepository;
		this.imageRepository = imageRepository;
		this.hostDomain = hostDomain;
	}

	@Override
	public Collection<ArticleWithoutContent> getArticlesWithoutContent(String topic) {
		Collection<BlogArticle> articles = blogArticleRepository.findAllByTopicOrderByCreatedDesc(topic);
		return articles.stream().map(this::toDtoWithoutContent).toList();
	}

	@Transactional
	@Override
	public List<ArticleClientDto> getArticles(String topic) {
		Collection<BlogArticle> articles;
		if (topic == null || topic.isBlank()) {
			articles = blogArticleRepository.findAllByOrderByLastChangeDesc();
		} else {
			articles = blogArticleRepository.findAllByTopicOrderByCreatedDesc(topic);
		}
		return articles.stream().map(this::toDto).toList();
	}

	@Transactional
	@Override
	public ArticleClientDto getArticle(Long id) {
		Objects.requireNonNull(id);
		return toDto(blogArticleRepository.findById(id).orElseThrow());
	}

	@Transactional
	@Override
	public Long saveArticle(CreateArticleDto article, Collection<MultipartFile> images) {
		Objects.requireNonNull(article);
		BlogArticle articleEntity = BlogArticle.builder()
				.setTitle(new LocalizedText(article.title()))
				.setContent(new LocalizedText(article.content()))
				.setTopic(article.topic())
				.setSettings(article.appearanceSettings())
				.setDescription(article.description())
				.setLastChange(System.currentTimeMillis())
				.setCreated(System.currentTimeMillis())
				.build();
		Long articleID = blogArticleRepository.save(articleEntity).getId();
		if (images != null && !images.isEmpty()) {
			for (MultipartFile image : images) {
				toImageEntity(image, articleID);
			}
		}
		return articleID;
	}

	@Transactional
	@Override
	public void updateArticle(Long id, CreateArticleDto article, Collection<MultipartFile> images) {
		BlogArticle existingArticle = blogArticleRepository.findById(id).orElseThrow();

		if (article.appearanceSettings() != null) {
			existingArticle.setAppearanceSettings(article.appearanceSettings());
		}
		if (article.title() != null) {
			existingArticle.setTitle(new LocalizedText(article.title()));
		}
		if (article.topic() != null) {
			existingArticle.setTopic(article.topic());
		}
		if (article.content() != null) {
			existingArticle.setContent(new LocalizedText(article.content()));
		}
		if (article.description() != null) {
			existingArticle.setDescription(new LocalizedText(article.description()));
		}

		existingArticle.setLastChange(System.currentTimeMillis());
		blogArticleRepository.save(existingArticle);
		if (images != null && !images.isEmpty()) {
			for (MultipartFile image : images) {
				Image dbImage = imageRepository.getImageByRelatedEntityAndFilename(id, image.getOriginalFilename());
				if(dbImage == null) {
					toImageEntity(image, id);
				} else {
					dbImage.setData(getImageData(image));
					dbImage.setContentType(image.getContentType());
					imageRepository.save(dbImage);
				}
			}
		}

	}

	@Transactional
	@Override
	public void deleteArticle(Long id) {
		Objects.requireNonNull(id);
		blogArticleRepository.deleteWithImages(id);
	}

	@Transactional
	@Override
	public String saveTopic(TopicDto topic, MultipartFile image) {
		Objects.requireNonNull(topic);
		Image imageEntity = null;
		if (blogTopicRepository.existsById(topic.id())) {//is updating topic
			Image oldImg = blogTopicRepository.findById(topic.id()).orElseThrow().getImage();
			if (oldImg != null) {
				imageEntity = updateImage(oldImg, image);
			}
		}
		if(imageEntity == null) {
			imageEntity = toImageEntity(image);
		}
		BlogTopic blogTopic = BlogTopic.builder()
				.setId(topic.id().toLowerCase())
				.setName(new LocalizedText(topic.title()))
				.setDescription(new LocalizedText(topic.description()))
				.setImage(imageEntity)
				.setLastChange(System.currentTimeMillis()) //todo implement time utils to get current time in timezone
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
	 * Get data from an image file and save it to database.
	 * @param image entity to update
	 * @param imageFile to get new data from
	 * @return updated image entity
	 */
	Image updateImage(Image image, MultipartFile imageFile) {
		image.setData(getImageData(imageFile));
		image.setContentType(imageFile.getContentType());
		image.setFilename(imageFile.getOriginalFilename());
		return imageRepository.save(image);
	}

	/**
	 * Save image to database.
	 *
	 * @param image image to save
	 * @return image entity
	 */
	private Image toImageEntity(MultipartFile image) {
		return toImageEntity(image, null);
	}

	/**
	 * Save image to database.
	 *
	 * @param image image to save
	 * @return image entity
	 */
	private Image toImageEntity(MultipartFile image, Long relatedEntity) {
		byte[] imgData = getImageData(image);
		Image imageEntity =
				Image.builder()
						.setContentType(image.getContentType())
						.setFilename(image.getOriginalFilename())
						.setRelatedEntity(relatedEntity)
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
		String image = hostDomain + "/images/topic/" + topic.getId()+"?="+topic.getLastChange();
		return new TopicClientDto(
				topic.getId(),
				topic.getName().getTranslations(),
				topic.getDescription().getTranslations(),
				image,
				topic.getLastChange()
		);
	}

	private ArticleClientDto toDto(BlogArticle article) {
		return new ArticleClientDto(
				article.getId(),
				article.getTitle().getTranslations(),
				article.getDescription() == null ? null : article.getContent().getTranslations(),
				article.getDescription() == null ? null : article.getDescription().getTranslations(),
				article.getAppearanceSettings(),
				article.getTopic(),
				article.getLastChange(),
				article.getCreated()
		);
	}

	private ArticleWithoutContent toDtoWithoutContent(BlogArticle article) {
		return new ArticleWithoutContent(
				article.getId(),
				article.getTitle().getTranslations(),
				article.getDescription() == null ? null : article.getDescription().getTranslations(),
				article.getAppearanceSettings(),
				article.getTopic(),
				article.getLastChange(),
				article.getCreated()
		);
	}

	private FullBlogDto toDto(BlogTopic topic, List<BlogArticle> articles) {
		return new FullBlogDto(toDto(topic), articles.stream().map(this::toDtoWithoutContent).toArray(ArticleWithoutContent[]::new));
	}
}
