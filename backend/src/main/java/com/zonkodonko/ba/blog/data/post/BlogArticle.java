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
@Table(name = "blog_article")
public final class BlogArticle implements com.zonkodonko.ba.storage.Entity<Long> {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	private Long createdDate;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private LocalizedText title;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private LocalizedText content;

	@OneToMany(
			mappedBy = "article",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY,
			orphanRemoval = true)
	private List<Image> images;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private ArticleSettings postSettings;

	private String topic; //todo add foreign key constraint


	public BlogArticle() {
	}

	public BlogArticle(
			LocalizedText title,
			LocalizedText content,
			Long createdDate,
			List<Image> images,
			ArticleSettings postSettings,
			String topic) {
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.images = images;
		this.postSettings = postSettings;
		this.topic = topic;
	}

	public BlogArticle(
			Long id,
			LocalizedText title,
			LocalizedText content,
			Long createdDate,
			List<Image> images,
			ArticleSettings postSettings,
			String topic) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.images = images;
		this.postSettings = postSettings;
		this.topic = topic;
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

	public Long getCreatedDate() {
		return createdDate;
	}

	public List<Image> getImages() {
		return images;
	}

	public ArticleSettings getPostSettings() {
		return postSettings;
	}

	public String getTopic() {
		return topic;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (BlogArticle) obj;
		return Objects.equals(this.id, that.id) &&
				Objects.equals(this.title, that.title) &&
				Objects.equals(this.content, that.content) &&
				Objects.equals(this.createdDate, that.createdDate) &&
				Objects.equals(this.images, that.images) &&
				Objects.equals(this.postSettings, that.postSettings) &&
				Objects.equals(this.topic, that.topic);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, content, createdDate, images, postSettings, topic);
	}

	@Override
	public String toString() {
		return "BlogPost[" +
				"id=" + id + ", " +
				"getTitle=" + title + ", " +
				"getContent=" + content + ", " +
				"createdDate=" + createdDate + ", " +
				"image=" + images + ", " +
				"postSettings=" + postSettings + ", " +
				"getTopic=" + topic + ']';
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Long id;
		private LocalizedText title;
		private LocalizedText content;
		private Long createdDate;
		private List<Image> images;
		private ArticleSettings postSettings;
		private String topic;
		private String defaultLanguage = "de";

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

		public Builder setContent(LocalizedText content) {
			this.content = content;
			return this;
		}

		public Builder setContent(String content) {
			this.content = new LocalizedText(Map.of(defaultLanguage, content));
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

		public Builder setCreatedDate(Long createdDate) {
			this.createdDate = createdDate;
			return this;
		}

		public Builder setImages(List<Image> images) {
			this.images = images;
			return this;
		}

		public Builder setSettings(ArticleSettings postSettings) {
			this.postSettings = postSettings;
			return this;
		}

		public Builder setTopic(String topic) {
			this.topic = topic;
			return this;
		}

		public BlogArticle build() {
			return new BlogArticle(id, title, content, createdDate, images, postSettings, topic);
		}
	}
}
