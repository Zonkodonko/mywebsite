package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.EntityType;
import com.zonkodonko.ba.blog.data.images.Image;

import java.util.Collection;

public interface ImageService {

	Image getImage(long imageId);

	/**
	 * Returns image by type and id of related entity.
	 *
	 * @param type Image type
	 * @param id   Related entity id
	 * @return Image
	 */
	@Deprecated
	Image getImageBy(EntityType type, String id);

	/**
	 * Returns image by type and id of related entity and filename of the image.
	 *
	 * @param type     related entity type
	 * @param id       related entity id
	 * @param filename image filename
	 * @return image
	 */
	Image getImageBy(EntityType type, String id, String filename);

	/**
	 * Returns all images related to entity of given type and id.
	 *
	 * @param type related entity type
	 * @param id   of related entity
	 * @return collection of images
	 */
	Collection<Image> getAllByRelatedEntity(EntityType type, String id);

	/**
	 * Deletes image for related article and filename.
	 * @param id Article id
	 * @param filename Image filename
	 */
	void deleteForEntity(Long id, String filename);

}
