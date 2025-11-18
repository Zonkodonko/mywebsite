package com.zonkodonko.ba.blog;

import com.zonkodonko.ba.blog.data.images.Image;
import com.zonkodonko.ba.blog.data.images.ImageRepository;
import org.springframework.stereotype.Component;

/**
 * todo write comment
 *
 * @author Timm
 * @version 17.11.2025
 */
@Component
public class ImageServiceImpl implements ImageService {

	final ImageRepository imageRepository;

	public ImageServiceImpl(ImageRepository imageRepository) {
		this.imageRepository = imageRepository;
	}

	@Override
	public Image getImage(long imageId) {
		return imageRepository.findById(imageId).orElse(null);
	}
}
