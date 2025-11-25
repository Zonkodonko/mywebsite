package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.images.Image;
import com.zonkodonko.ba.blog.data.images.ImageRepository;
import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.post.BlogArticleRepository;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import com.zonkodonko.ba.blog.data.topic.BlogTopicRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

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
	public Image getImageBy(String type, String id) {
		if(type.equals("article")) {
			BlogArticle article = blogArticleRepository.findById(Long.valueOf(id)).orElseThrow(() -> new IllegalArgumentException("Article not found"));
			Hibernate.initialize(article.getImages());
			return article.getImages().get(0);
		} else if(type.equals("topic")) {
			BlogTopic topic = blogTopicRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Topic not found"));
			Hibernate.initialize(topic.getImage());
			return topic.getImage();
		}
		return null;
	}
}
