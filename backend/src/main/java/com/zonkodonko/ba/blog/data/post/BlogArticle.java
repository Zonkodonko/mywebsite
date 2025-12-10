package com.zonkodonko.ba.blog.data.post;

import com.zonkodonko.ba.blog.data.images.Image;
import com.zonkodonko.ba.storage.LocalizedText;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Blog post entity.
 */
@Entity
@Table(name = "blog_articles")
public final class BlogArticle implements com.zonkodonko.ba.storage.Entity<Long> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "last_change")
	private Long lastChange;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private LocalizedText title;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private LocalizedText content;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private ArticleSettings appearanceSettings;
	
	@Column(name = "created")
	private Long created;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private LocalizedText description;

	private String topic; //todo add foreign key constraint


	public BlogArticle() {
	}

	public BlogArticle(
			LocalizedText title,
			LocalizedText content,
			LocalizedText description,
			Long lastChange,
			ArticleSettings appearanceSettings,
			String topic,
			Long created) {
		this.title = title;
		this.content = content;
		this.description = description;
		this.lastChange = lastChange;
		this.appearanceSettings = appearanceSettings;
		this.topic = topic;
		this.created = created;
	}

	public BlogArticle(
			Long id,
			LocalizedText title,
			LocalizedText content,
			LocalizedText description,
			Long lastChange,
			ArticleSettings appearanceSettings,
			String topic,
			Long created) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.description = description;
		this.lastChange = lastChange;
		this.appearanceSettings = appearanceSettings;
		this.topic = topic;
		this.created = created;
	}

	public LocalizedText getDescription() {
		return description;
	}

	public void setDescription(LocalizedText description) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public LocalizedText getTitle() {
		return title;
	}

	public LocalizedText getContent() {
		return content;
	}

	public Long getLastChange() {
		return lastChange;
	}

	public ArticleSettings getAppearanceSettings() {
		return appearanceSettings;
	}

	public String getTopic() {
		return topic;
	}

	public Long getCreated() {
		return created;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setLastChange(Long lastChange) {
		this.lastChange = lastChange;
	}

	public void setTitle(LocalizedText title) {
		this.title = title;
	}

	public void setContent(LocalizedText content) {
		this.content = content;
	}

	public void setAppearanceSettings(ArticleSettings appearanceSettings) {
		this.appearanceSettings = appearanceSettings;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public void setTopic(String topic) {
		this.topic = topic;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (BlogArticle) obj;
		return Objects.equals(this.id, that.id) &&
				Objects.equals(this.title, that.title) &&
				Objects.equals(this.content, that.content) &&
				Objects.equals(this.description, that.description) &&
				Objects.equals(this.lastChange, that.lastChange) &&
				Objects.equals(this.appearanceSettings, that.appearanceSettings) &&
				Objects.equals(this.topic, that.topic) &&
				Objects.equals(this.created, that.created);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, content, description, lastChange, appearanceSettings, topic, created);
	}

	@Override
	public String toString() {
		return "BlogPost[" +
				"id=" + id + ", " +
				"title=" + title + ", " +
				"content=" + content + ", " +
				"description=" + description + ", " +
				"lastChange=" + lastChange + ", " +
				"postSettings=" + appearanceSettings + ", " +
				"topic=" + topic + ", " +
				"created=" + created + ']';
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Long id;
		private LocalizedText title;
		private LocalizedText content;
		private LocalizedText description;
		private Long lastChange;
		private List<Image> images;
		private ArticleSettings appearanceSettings;
		private String topic;
		private Long created;
		private String defaultLanguage = "de";

		public Builder setDescription(LocalizedText description) {
			this.description = description;
			return this;
		}

		public Builder setDescription(String description) {
			this.description = new LocalizedText(Map.of(defaultLanguage, description));
			return this;
		}

		public Builder setDescription(Map<String, String> description) {
			this.description = new LocalizedText(description);
			return this;
		}

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}


		public Builder setTitle(LocalizedText title) {
			this.title = title;
			return this;
		}

		public Builder setTitle(String title) {
			this.title = new LocalizedText(Map.of(defaultLanguage, title));
			return this;
		}

		public Builder setTitle(Map<String, String> title) {
			this.title = new LocalizedText(title);
			return this;
		}

		public Builder setContent(LocalizedText content) {
			this.content = content;
			return this;
		}

		public Builder setContent(String content) {
			this.content = new LocalizedText(Map.of(defaultLanguage, content));
			return this;
		}

		public Builder setContent(Map<String, String> content) {
			this.content = new LocalizedText(content);
			return this;
		}

		/**
		 * Set default language for texts
		 *
		 * @param defaultLanguage Language code
		 * @return Builder
		 */
		public Builder setDefaultLanguage(String defaultLanguage) {
			this.defaultLanguage = defaultLanguage;
			return this;
		}

		public Builder setLastChange(Long createdDate) {
			this.lastChange = createdDate;
			return this;
		}

		public Builder setImages(List<Image> images) {
			this.images = images;
			return this;
		}

		public Builder setSettings(ArticleSettings postSettings) {
			this.appearanceSettings = postSettings;
			return this;
		}

		public Builder setTopic(String topic) {
			this.topic = topic;
			return this;
		}

		public Builder setCreated(Long created) {
			this.created = created;
			return this;
		}

		public BlogArticle build() {
			return new BlogArticle(id, title, content, description, lastChange, appearanceSettings, topic, created);
		}
	}
}
