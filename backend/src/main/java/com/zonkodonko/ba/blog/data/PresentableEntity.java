package com.zonkodonko.ba.blog.data;


import com.zonkodonko.ba.blog.data.images.Image;
import com.zonkodonko.ba.storage.Entity;

/**
 *
 * @param <ID_TYPE>
 */
public interface PresentableEntity<ID_TYPE> extends Entity<ID_TYPE> {

	Image getImage();

}
