package com.zonkodonko.ba.blog;


import com.zonkodonko.ba.blog.data.BlogArticle;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Service to interact with blog articles.
 */
public interface BlogArticleService {

	/**
	 * Get articles by topic.
	 *
	 * @param topic Topic name
	 * @return List of blog articles sorted by created date in descending order
	 */
	List<BlogArticle> getArticles(@NotNull String topic);

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
	Long saveArticle(@NotNull BlogArticle article);

	/**
	 * Delete article by id.
	 *
	 * @param id Article id
	 */
	void deleteArticle(@NotNull Long id);
}
