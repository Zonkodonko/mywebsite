package com.zonkodonko.ba.blog.data.images;

import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

/**
 * Repository for {@link Image}.
 */
public interface ImageRepository extends CrudRepository<Image, Long> {


	List<Image> getImagesByRelatedEntity(Long relatedEntity);

	Image getImageByRelatedEntityAndFilename(Long relatedEntity, String filename);

	void deleteByRelatedEntity(Long relatedEntity);

	List<Image> findAllByRelatedEntityAndFilename(Long relatedEntity, String filename);

	Image findByRelatedEntityAndFilename(Long relatedEntity, String filename);

	Collection<Image> findAllByRelatedEntity(Long relatedEntity);

	void deleteAllByRelatedEntityAndFilename(Long relatedEntity, String filename);
}
