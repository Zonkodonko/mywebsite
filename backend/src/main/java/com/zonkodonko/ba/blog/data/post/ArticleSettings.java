package com.zonkodonko.ba.blog.data.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Settings for a blog post.
 *
 * @param imagePosition position where to place image in post.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public record ArticleSettings(ImagePosition imagePosition) {

	@JsonFormat(shape = JsonFormat.Shape.OBJECT)
	public enum ImagePosition {
		LEFT, RIGHT, TOP, BOTTOM;

		@JsonValue
		@Override
		public String toString() {
			return super.toString();
		}
	}

}
