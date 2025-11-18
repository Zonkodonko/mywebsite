package com.zonkodonko.ba.blog.rest.dtos;

import com.zonkodonko.ba.storage.LocalizedText;

/**
 * Contains getTopic data to create new getTopic.
 *
 * @author Timm
 * @version 14.11.2025
 */
public record TopicDto(String id, LocalizedText title, LocalizedText description) {
}
