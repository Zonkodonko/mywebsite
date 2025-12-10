package com.zonkodonko.ba.blog.rest.dtos.incoming;

import java.util.Map;

/**
 * Contains topic data to create new topic.
 *
 * @author Timm
 * @version 14.11.2025
 */
public record TopicDto(String id, Map<String, String> title, Map<String, String> description) {
}
