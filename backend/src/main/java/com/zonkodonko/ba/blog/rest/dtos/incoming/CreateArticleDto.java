package com.zonkodonko.ba.blog.rest.dtos.incoming;

import com.zonkodonko.ba.blog.data.post.ArticleSettings;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

/**
 * Dto to receive new articles
 *
 * @author Timm
 * @version 14.11.2025
 */
public record CreateArticleDto(
		@NotNull
		Map<String, String> title,
		Map<String, String> content,
		@NotNull
		Map<String, String> description,
		ArticleSettings appearanceSettings,
		String topic) {
}
