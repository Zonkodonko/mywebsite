package com.zonkodonko.ba.blog;


import com.zonkodonko.ba.blog.data.post.BlogArticle;
import com.zonkodonko.ba.blog.data.topic.BlogTopic;
import com.zonkodonko.ba.blog.rest.dtos.ArticleClientDto;
import com.zonkodonko.ba.blog.rest.dtos.CreateArticleDto;
import com.zonkodonko.ba.blog.rest.dtos.TopicClientDto;
import com.zonkodonko.ba.blog.rest.dtos.TopicDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

/**
 * Service to interact with blog articles.
 */
public interface BlogService {

	/**
	 * Get articles by getTopic.
	 *
	 * @param topic Topic name
	 * @return List of blog articles sorted by created date in descending order
	 */
	List<ArticleClientDto> getArticles(@NotNull String topic);

	/**
	 * Get article by id.
	 *
	 * @param id Article id
	 * @return Article
	 */
	BlogArticle getArticle(@NotNull Long id);

	/**
	 * Create or update article.
	 *
	 * @param article Article to save
	 * @return id of saved article
	 */
	Long saveArticle(@NotNull CreateArticleDto article, MultipartFile image);

	/**
	 * Delete article by id.
	 *
	 * @param id Article id
	 */
	void deleteArticle(@NotNull Long id);

	/**
	 * Save getTopic.
	 * @param topic Topic to save
	 */
	String saveTopic(@NotNull TopicDto topic, MultipartFile image);

	/**
	 * Get all topics.
	 * @return Collection of topics
	 */
	Collection<TopicClientDto> getTopics();

	/**
	 * Get getTopic by id.
	 * @param id Topic id
	 * @return Topic
	 */
	BlogTopic getTopic(@NotNull String id);

	/**
	 * Delete getTopic by id.
	 * @param id Topic id
	 */
	void deleteTopic(@NotNull String id);

	/**
	 * Delete getTopic by id and all articles associated with it.
	 * @param id Topic id
	 * @param withArticles Delete articles associated with getTopic?
	 */
	void deleteTopic(@NotNull String id, boolean withArticles);
}
