package com.zonkodonko.ba.blog.data.topic;

import com.zonkodonko.ba.blog.data.images.Image;
import com.zonkodonko.ba.storage.LocalizedText;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Objects;

/**
 * Topic entity
 *
 * @author Timm
 * @version 14.11.2025
 */
@Entity
@Table(name = "blog_topics")
public final class BlogTopic {

	@Id
	private String id;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private LocalizedText name;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private LocalizedText description;

	@OneToOne(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL,
			orphanRemoval = true)
	private Image image;


	public BlogTopic() {
	}

	/**
	 *
	 */
	public BlogTopic(String id, LocalizedText name, LocalizedText description, Image image) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public LocalizedText getName() {
		return name;
	}

	public LocalizedText getDescription() {
		return description;
	}

	public Image getImage() {
		return image;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (BlogTopic) obj;
		return Objects.equals(this.id, that.id) &&
				Objects.equals(this.name, that.name) &&
				Objects.equals(this.description, that.description) &&
				Objects.equals(this.image, that.image);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description, image);
	}

	@Override
	public String toString() {
		return "BlogTopic[" +
				"id=" + id + ", " +
				"name=" + name + ", " +
				"description=" + description + ", " +
				"image=" + (image != null ? image.getFilename() : "null") + ']';
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String id;
		private LocalizedText name;
		private LocalizedText description;
		private Image image;
		private String defaultLanguage = "de"; // Default language is German

		private Builder() {
		}

		public Builder setId(String id) {
			this.id = id;
			return this;
		}

		public Builder setName(LocalizedText name) {
			this.name = name;
			return this;
		}

		public Builder setName(String name) {
			if (this.name == null) {
				this.name = new LocalizedText();
			}
			this.name.put(defaultLanguage, name);
			return this;
		}

		public Builder setDescription(LocalizedText description) {
			this.description = description;
			return this;
		}

		public Builder setDescription(String description) {
			if (this.description == null) {
				this.description = new LocalizedText();
			}
			this.description.put(defaultLanguage, description);
			return this;
		}

		public Builder setImage(Image image) {
			this.image = image;
			return this;
		}

		public Builder setDefaultLanguage(String language) {
			this.defaultLanguage = language;
			return this;
		}

		public BlogTopic build() {
			return new BlogTopic(id, name, description, image);
		}
	}

}
	
	
	
	

