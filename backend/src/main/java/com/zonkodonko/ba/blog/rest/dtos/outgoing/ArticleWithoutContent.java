package com.zonkodonko.ba.blog.rest.dtos.outgoing;

import com.zonkodonko.ba.blog.data.post.ArticleSettings;

import java.util.Map;

public record ArticleWithoutContent(
		Long id,
		Map<String, String> title,
		Map<String, String> description,
		ArticleSettings appearanceSettings,
		String topic,
		Long lastChange,
		Long created

) {
}
