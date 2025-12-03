package com.zonkodonko.ba.blog.data;

import static org.junit.jupiter.api.Assertions.*;

import com.zonkodonko.ba.blog.data.post.ArticleSettings;
import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.post.BlogArticleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;


/**
 * Test class for BlogPostRepository operations
 *
 * @author Timm
 * @version 14.11.2025
 */
@SpringBootTest
public class TestBlogArticleData {

	@Autowired
	private BlogArticleRepository blogPostRepository;

	private BlogArticle testPost;

	@BeforeEach
	void setUp() {
		testPost = BlogArticle.builder().setTitle("Test Title")
				.setContent("Test Content")
				.setSettings(new ArticleSettings(ArticleSettings.ImagePosition.TOP,"title.jpg"))
				.setLastChange(System.currentTimeMillis())
				.build();
	}

	@AfterEach
	void tearDown() {
		blogPostRepository.deleteAll();
	}

	@Test
	void testAddBlogPost() {
		BlogArticle savedPost = blogPostRepository.save(testPost);
		assertNotNull(savedPost.getId());
		assertEquals(testPost.getTitle(), savedPost.getTitle());
		Optional<BlogArticle> fromDb = blogPostRepository.findById(savedPost.getId());
		assertTrue(fromDb.isPresent());
		assertEquals(savedPost.getAppearanceSettings(), fromDb.get().getAppearanceSettings());
	}

	@Test
	void testDeleteBlogPost() {
		BlogArticle savedPost = blogPostRepository.save(testPost);
		blogPostRepository.deleteById(savedPost.getId());
		assertFalse(blogPostRepository.existsById(savedPost.getId()));
	}
}
