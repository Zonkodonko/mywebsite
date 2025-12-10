package com.zonkodonko.ba.blog.rest.dtos.outgoing;

import com.zonkodonko.ba.blog.data.post.ArticleSettings;

import java.util.Map;

/**
 *
 * Article data transfer object to send to client.
 *
 * @param topic    topic id
 * @param appearanceSettings article appearanceSettings
 * @author Timm
 * @version 18.11.2025
 */
public record ArticleClientDto(
		Long id,
		Map<String, String> title,
		Map<String, String> content,
		Map<String, String> description,
		ArticleSettings appearanceSettings,
		String topic,
		Long lastChange,
		Long created
) {
}
