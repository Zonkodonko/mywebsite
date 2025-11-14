package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.storage.LocalizedText;

/**
 * Contains topic data to create new topic.
 *
 * @author Timm
 * @version 14.11.2025
 */
public record TopicDto(String id, LocalizedText title, LocalizedText description) {
}
