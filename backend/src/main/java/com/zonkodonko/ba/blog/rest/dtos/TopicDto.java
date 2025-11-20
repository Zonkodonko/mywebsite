package com.zonkodonko.ba.blog.rest.dtos;

import com.zonkodonko.ba.storage.LocalizedText;

import java.util.Map;

/**
 * Contains getTopic data to create new getTopic.
 *
 * @author Timm
 * @version 14.11.2025
 */
public record TopicDto(String id, Map<String,String> title, Map<String,String> description) {
}
