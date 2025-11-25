package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.images.Image;

public interface ImageService {

	Image getImage(long imageId);

	Image getImageBy(String type, String id);

}
