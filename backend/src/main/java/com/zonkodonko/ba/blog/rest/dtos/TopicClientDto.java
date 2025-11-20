package com.zonkodonko.ba.blog.rest.dtos;

import com.zonkodonko.ba.storage.LocalizedText;

import java.util.Map;

/**
 * Topic data transfer object to send to client.
 * @param id topic id
 * @param title topic title
 * @param description topic description
 * @param image BASE64 encoded image
 */
public record TopicClientDto(String id, Map<String, String> title, Map<String, String> description, String image) {
}
