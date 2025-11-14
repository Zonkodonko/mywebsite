package com.zonkodonko.ba.blog.data.topic;

import com.zonkodonko.ba.storage.LocalizedText;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@Table(name = "topics")
public final class BlogTopic {

	@Id
	private String id;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private LocalizedText name;

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(columnDefinition = "jsonb")
	private LocalizedText description;

	public BlogTopic() {
	}

	/**
	 *
	 */
	public BlogTopic(String id, LocalizedText name, LocalizedText description) {
		this.id = id;
		this.name = name;
		this.description = description;
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

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (BlogTopic) obj;
		return Objects.equals(this.id, that.id) &&
				Objects.equals(this.name, that.name) &&
				Objects.equals(this.description, that.description);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, description);
	}

	@Override
	public String toString() {
		return "BlogTopic[" +
				"id=" + id + ", " +
				"name=" + name + ", " +
				"description=" + description + ']';
	}

}
