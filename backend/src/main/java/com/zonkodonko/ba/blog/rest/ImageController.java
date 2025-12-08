package com.zonkodonko.ba.blog.rest;

import com.zonkodonko.ba.blog.ImageService;
import com.zonkodonko.ba.blog.data.EntityType;
import com.zonkodonko.ba.blog.data.images.Image;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * todo write comment
 *
 * @author Timm
 * @version 17.11.2025
 */
@RestController
@RequestMapping("/images")
public class ImageController {


	final ImageService imageService;

	ImageController(ImageService imageService) {
		this.imageService = imageService;
	}


	@GetMapping("{imageId}")
	public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
		Image image = imageService.getImage(imageId);

		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(image.getContentType()))
				.cacheControl(CacheControl.maxAge(7, TimeUnit.DAYS).cachePublic())
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
				.body(image.getData());
	}

	@GetMapping("/{entityType}/{entityId}")
	public ResponseEntity<byte[]> getImage(@PathVariable String entityType, @PathVariable String entityId) {
		Image image = imageService.getImageBy(EntityType.valueOf(entityType.toUpperCase()), entityId);
		if(image == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(image.getContentType()))
				.cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
				.body(image.getData());
	}

	@GetMapping("/{entityType}/{entityId}/{filename}")
	public ResponseEntity<byte[]> getImageFromEntity(@PathVariable String entityType, @PathVariable String entityId, @PathVariable String filename) {
		Image image = imageService.getImageBy(EntityType.valueOf(entityType.toUpperCase()), entityId, filename);
		if(image == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(image.getContentType()))
				.cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
				.header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + image.getFilename() + "\"")
				.body(image.getData());
	}

	@GetMapping("/{entityType}/{entityId}/all")
	public ResponseEntity<byte[]> getImagesFromEntity(@PathVariable String entityType, @PathVariable String entityId) {
		Collection<Image> images = imageService.getAllByRelatedEntity(EntityType.valueOf(entityType.toUpperCase()), entityId);
		if (images.isEmpty()) {
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(new byte[0]);
		}
		ByteArrayOutputStream zipStream = zipImages(images);
		return ResponseEntity.ok()
				.cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS).cachePublic())
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"images.zip\"")
				.body(zipStream.toByteArray());
	}

	private ByteArrayOutputStream zipImages(Collection<Image> images) {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
		     ZipOutputStream zos = new ZipOutputStream(baos)) {
			for (Image image : images) {
				ZipEntry entry = new ZipEntry(image.getFilename());
				zos.putNextEntry(entry);
				zos.write(image.getData());
				zos.closeEntry();
			}
			zos.close();
			return baos;

		} catch (IOException e) {
			throw new RuntimeException("Failed to zip images", e);
		}
	}


	/**
	 * Erkennt den MediaType basierend auf den Magic Bytes des Bildes
	 */
	private MediaType determineMediaType(byte[] imageData) {
		if (imageData.length < 4) {
			return MediaType.APPLICATION_OCTET_STREAM;
		}

		// PNG: 89 50 4E 47
		if (imageData[0] == (byte) 0x89 && imageData[1] == 0x50 &&
				imageData[2] == 0x4E && imageData[3] == 0x47) {
			return MediaType.IMAGE_PNG;
		}

		// JPEG: FF D8 FF
		if (imageData[0] == (byte) 0xFF && imageData[1] == (byte) 0xD8 &&
				imageData[2] == (byte) 0xFF) {
			return MediaType.IMAGE_JPEG;
		}

		// GIF: 47 49 46 38
		if (imageData[0] == 0x47 && imageData[1] == 0x49 &&
				imageData[2] == 0x46 && imageData[3] == 0x38) {
			return MediaType.IMAGE_GIF;
		}

		// WebP: 52 49 46 46 (RIFF)
		if (imageData[0] == 0x52 && imageData[1] == 0x49 &&
				imageData[2] == 0x46 && imageData[3] == 0x46) {
			return MediaType.parseMediaType("image/webp");
		}

		return MediaType.APPLICATION_OCTET_STREAM;
	}
}
