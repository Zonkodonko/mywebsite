package com.zonkodonko.ba.blog.rest.dtos;

import com.zonkodonko.ba.blog.data.post.ArticleSettings;

import java.util.Map;

/**
 * Dto to receive new articles
 *
 * @author Timm
 * @version 14.11.2025
 */
public record CreateArticleDto(
		Map<String, String> title,
		Map<String, String> content,
		ArticleSettings appearanceSettings,
		String topic) {
}
