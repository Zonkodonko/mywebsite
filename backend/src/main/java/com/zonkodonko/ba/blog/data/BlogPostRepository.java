package com.zonkodonko.ba.blog.data;

import org.springframework.data.repository.CrudRepository;

public interface BlogPostRepository extends CrudRepository<BlogArticle, Long> {
}
