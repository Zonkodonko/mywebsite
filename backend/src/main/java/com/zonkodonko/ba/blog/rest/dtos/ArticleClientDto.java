package com.zonkodonko.ba.blog.rest.dtos;

import com.zonkodonko.ba.blog.data.post.ArticleSettings;
import com.zonkodonko.ba.storage.LocalizedText;

/**
 *
 * Article data transfer object to send to client.
 *
 * @param topic topic id
 * @param settings article settings
 *
 * @author Timm
 * @version 18.11.2025
 */
public record ArticleClientDto(
		Long id,
		LocalizedText title,
		LocalizedText content,
		ArticleSettings settings,
		String topic
) {
}
