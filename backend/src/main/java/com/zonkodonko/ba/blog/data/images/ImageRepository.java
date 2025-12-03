package com.zonkodonko.ba.blog.data.images;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Repository for {@link Image}.
 */
public interface ImageRepository extends CrudRepository<Image, Long> {


	List<Image> getImagesByRelatedEntity(Long relatedEntity);
}
