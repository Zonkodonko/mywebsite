package com.zonkodonko.ba.blog.rest.dtos;

public record FullBlogDto(
		TopicClientDto topic,
		ArticleClientDto[] articles
) {
}
