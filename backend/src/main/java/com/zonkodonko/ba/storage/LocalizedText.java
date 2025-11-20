package com.zonkodonko.ba.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Text in multiple languages
 *
 * @author Timm
 * @version 14.11.2025
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public class LocalizedText {
	private Map<String, String> translations = new HashMap<>();

	public LocalizedText() {
	}

	public LocalizedText(Map<String, String> translations) {
		this.translations = translations;
	}


	@JsonCreator
	static LocalizedText fromMap(Map<String, String> translations) {
		return new LocalizedText(translations);
	}

	/**
	 * Get text for language
	 *
	 * @param language Language code
	 * @return Text for language
	 */
	public String get(String language) {
		return translations.getOrDefault(language, translations.get("de")); // Fallback auf Deutsch
	}

	public void put(String language, String text) {
		translations.put(language, text);
	}

	@JsonValue
	public Map<String, String> getTranslations() {
		return translations;
	}

	public void setTranslations(Map<String, String> translations) {
		this.translations = translations;
	}
}

