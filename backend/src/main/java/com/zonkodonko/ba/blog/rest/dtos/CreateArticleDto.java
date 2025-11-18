package com.zonkodonko.ba.blog.rest.dtos;

import com.zonkodonko.ba.blog.data.post.ArticleSettings;
import com.zonkodonko.ba.storage.LocalizedText;

/**
 * Dto to receive new articles
 *
 * @author Timm
 * @version 14.11.2025
 */
public record CreateArticleDto(
		Long id,
		Long createdDate,
		LocalizedText getTitle,
		LocalizedText getContent,
		ArticleSettings getSettings,
		String getTopic){
}
