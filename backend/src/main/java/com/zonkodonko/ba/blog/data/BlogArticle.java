package com.zonkodonko.ba.blog.data;

import com.zonkodonko.ba.blog.data.post.ArticleSettings;
import com.zonkodonko.ba.storage.LocalizedText;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;
import java.util.Objects;

@Entity
@Table(name = "blog_article")
public final class BlogArticle implements com.zonkodonko.ba.resume.data.Entity<Long> {
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
	@Lob
	private byte[] image;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private ArticleSettings postSettings;


	public BlogArticle() {
	}

	public BlogArticle(
			LocalizedText title,
			LocalizedText content,
			Long createdDate,
			byte[] image,
			ArticleSettings postSettings) {
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.image = image;
		this.postSettings = postSettings;
	}

	public BlogArticle(
			Long id,
			LocalizedText title,
			LocalizedText content,
			Long createdDate,
			byte[] image,
			ArticleSettings postSettings) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.createdDate = createdDate;
		this.image = image;
		this.postSettings = postSettings;
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

	public byte[] getImage() {
		return image;
	}

	public ArticleSettings getPostSettings() {
		return postSettings;
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
				Objects.equals(this.image, that.image) &&
				Objects.equals(this.postSettings, that.postSettings);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, content, createdDate, image, postSettings);
	}

	@Override
	public String toString() {
		return "BlogPost[" +
				"id=" + id + ", " +
				"title=" + title + ", " +
				"content=" + content + ", " +
				"createdDate=" + createdDate + ", " +
				"image=" + image + ", " +
				"postSettings=" + postSettings + ']';
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Long id;
		private LocalizedText title;
		private LocalizedText content;
		private Long createdDate;
		private byte[] image;
		private ArticleSettings postSettings;
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

		public Builder setImage(byte[] image) {
			this.image = image;
			return this;
		}

		public Builder setSettings(ArticleSettings postSettings) {
			this.postSettings = postSettings;
			return this;
		}

		public BlogArticle build() {
			return new BlogArticle(id, title, content, createdDate, image, postSettings);
		}
	}
}
