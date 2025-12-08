package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.EntityType;
import com.zonkodonko.ba.blog.data.images.Image;
import com.zonkodonko.ba.blog.data.images.ImageRepository;
import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.post.BlogArticleRepository;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import com.zonkodonko.ba.blog.data.topic.BlogTopicRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * todo write comment
 *
 * @author Timm
 * @version 17.11.2025
 */
@Component
public class ImageServiceImpl implements ImageService {

	final ImageRepository imageRepository;

	final BlogArticleRepository blogArticleRepository;

	final BlogTopicRepository blogTopicRepository;

	public ImageServiceImpl(ImageRepository imageRepository, BlogArticleRepository blogArticleRepository, BlogTopicRepository blogTopicRepository) {
		this.imageRepository = imageRepository;
		this.blogArticleRepository = blogArticleRepository;
		this.blogTopicRepository = blogTopicRepository;
	}

	@Transactional
	@Override
	public Image getImage(long imageId) {
		return imageRepository.findById(imageId).orElse(null);
	}

	@Transactional
	@Override
	public Image getImageBy(EntityType type, String id) {
		if(type.equals(EntityType.ARTICLE)) {
			BlogArticle article = blogArticleRepository.findById(Long.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("Article not found"));
			List<Image> images = imageRepository.getImagesByRelatedEntity(article.getId());
			return images.getFirst();
		} else if(type.equals(EntityType.TOPIC)) {
			return getTopicImage(id);
		}
		return null;
	}

	@Transactional
	@Override
	public Image getImageBy(EntityType type, String id, String filename) {
		Image image = null;
		if(type.equals(EntityType.ARTICLE)) {
			image = imageRepository.findByRelatedEntityAndFilename(Long.valueOf(id), filename);
		} else if(type.equals(EntityType.TOPIC)) {
			image = getTopicImage(id);
		}
		return image;
	}

	private Image getTopicImage(String id) {
		BlogTopic topic = blogTopicRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Topic not found"));
		Hibernate.initialize(topic.getImage());
		return topic.getImage();
	}

	@Transactional
	@Override
	public Collection<Image> getAllByRelatedEntity(EntityType type, String id) {
		Collection<Image> images = null;
		if(type.equals(EntityType.ARTICLE)) {
			images = imageRepository.findAllByRelatedEntity(Long.valueOf(id));
		} else if(type.equals(EntityType.TOPIC)) {
			images = List.of(getTopicImage(id));
		}
		return images;
	}

	@Transactional
	@Override
	public void deleteForEntity(Long id, String filename) {
		Objects.requireNonNull(id);
		Objects.requireNonNull(filename);
		imageRepository.deleteAllByRelatedEntityAndFilename(id, filename);
	}
}
