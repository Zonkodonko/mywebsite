package com.zonkodonko.ba.resume.data;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * todo write comment
 *
 * @author Z0nko
 * @version 15.08.2025
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TestDbConnection {

	@Autowired
	SessionFactory sessionFactory;

	@Test
	public void testDbConnection() {
		Skill skill = Skill.builder()
				.setName("Typescript")
				.setLevel((byte) 1)
				.setCategory("Programming language")
				.setDescription("Programming language for web stuff")
				.build();

		Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		session.persist(skill);
		transaction.commit();
		session.close();
	}
}
