package com.zonkodonko.ba.resume.data;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * todo write comment
 *
 * @author Z0nko
 * @version 15.08.2025
 */
@Configuration
public class DbConfig {

//	@Bean
//	public SessionFactory sessionFactory() {
//		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//
//		try {
//			return new MetadataSources(registry).buildMetadata().buildSessionFactory();
//		} catch (Exception e) {
//			StandardServiceRegistryBuilder.destroy(registry);
//			throw new RuntimeException(e);
//		}
//	}

}
