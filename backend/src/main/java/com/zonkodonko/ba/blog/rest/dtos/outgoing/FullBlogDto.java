package com.zonkodonko.ba.blog.rest.dtos.outgoing;

public record FullBlogDto(
		TopicClientDto topic,
		ArticleWithoutContent[] articles
) {
}
