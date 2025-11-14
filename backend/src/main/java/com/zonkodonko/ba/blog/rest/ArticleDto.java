package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.blog.data.post.ArticleSettings;
import com.zonkodonko.ba.storage.LocalizedText;

/**
 * Dto to receive new articles
 *
 * @author Timm
 * @version 14.11.2025
 */
public record ArticleDto(Long id, Long createdDate, LocalizedText title, LocalizedText content, ArticleSettings settings, String topic){
}
