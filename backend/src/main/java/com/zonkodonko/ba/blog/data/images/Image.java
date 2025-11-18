package com.zonkodonko.ba.blog.data.images;

import jakarta.persistence.*;

@Entity
@Table(name = "image")
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(nullable = false)
	private String filename;

	@Column(nullable = false)
	private String contentType;

	private Long relatedEntity;

	@Lob
	@Column(nullable = false)
	private byte[] data;

	@Column(nullable = false)
	private Long size;


	public Image() {
	}

	public Image(String filename, String contentType, byte[] data, Long relatedEntity) {
		this.filename = filename;
		this.contentType = contentType;
		this.data = data;
		this.size = (long) data.length;
		this.relatedEntity = relatedEntity;
	}

	// Getters
	public Long getId() {
		return id;
	}

	public String getFilename() {
		return filename;
	}

	public String getContentType() {
		return contentType;
	}

	public Long getRelatedEntity() {
		return relatedEntity;
	}

	public byte[] getData() {
		return data;
	}

	/**
	 * Size in byte
	 * @return size of image in bytes.
	 */
	public Long getSize() {
		return size;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public void setData(byte[] data) {
		this.data = data;
		this.size = (long) data.length;
	}

	public static class Builder {
		private Long id;
		private String filename;
		private String contentType;
		private byte[] data;
		private Long size;

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setFilename(String filename) {
			this.filename = filename;
			return this;
		}

		public Builder setContentType(String contentType) {
			this.contentType = contentType;
			return this;
		}

		public Builder setData(byte[] data) {
			this.data = data;
			this.size = (long) data.length;
			return this;
		}

		public Image build() {
			Image image = new Image();
			image.setId(id);
			image.setFilename(filename);
			image.setContentType(contentType);
			image.setData(data);
			return image;
		}
	}

	public static Builder builder() {
		return new Builder();
	}
}
