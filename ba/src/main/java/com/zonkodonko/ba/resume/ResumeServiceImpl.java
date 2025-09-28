package com.zonkodonko.ba.resume;

import com.zonkodonko.ba.resume.data.CareerStep;
import com.zonkodonko.ba.resume.data.Entity;
import com.zonkodonko.ba.resume.data.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaDelete;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * todo write comment
 *
 * @author Z0nko
 * @version 29.08.2025
 */
@Service
public class ResumeServiceImpl implements ResumeService{

	@PersistenceContext
	private EntityManager entityManager;

	private String dataFolder = "data";

	public ResumeServiceImpl(@Value("${data.folder:data}") String dataFolder) {
		this.dataFolder = dataFolder;
	}

	@Transactional(readOnly = true)
	@Override
	public Resume getResume() {
		Collection<Skill> skills = entityManager.createQuery("SELECT s FROM Skill s", Skill.class).getResultList();
		Resume.Builder builder = Resume.builder();
		Locale locale = LocaleContextHolder.getLocale();
		if(!Locale.GERMAN.getLanguage().equals(locale.getLanguage())) {
			locale = Locale.ENGLISH;
		}
		System.out.println(locale.getLanguage());
		builder.setAboutMe(readAboutMe(locale.getLanguage()));
		builder.setSkills(skills);
		return builder.build();
	}

	@Transactional
	@Override
	public void updateSkills(Collection<Skill> skills) {
		updateAndDelete(skills, Skill.class);
	}

	@Transactional
	@Override
	public void updateCareer(Collection<CareerStep> careerSteps) {
		updateAndDelete(careerSteps, CareerStep.class);

	}

	@Override
	public void updateAboutMe(String lang, String aboutMe) {
		File dataFolder = new File("data");
		if(!dataFolder.exists()) {
			dataFolder.mkdir();
		}
		File file = new File("data/aboutme."+lang+".txt");
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(aboutMe);
			writer.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private String readAboutMe(String lang) {
		File file = new File("data/aboutme."+lang+".txt");
		if(!file.exists()) {
//			throw new IllegalStateException("About me file for language "+lang+" not found");
			return "";
		}
		try {
			return Files.readString(file.toPath());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void updateAndDelete(Collection<? extends Entity<Long>> entities, Class entityClass) {
		List<Entity<?>> newEntities = new ArrayList<>();
		List<Entity<?>> entitiesToUpdate = new ArrayList<>();

		for (Entity<?> entity : entities) {
			if(entity.getId() == null) {
				newEntities.add(entity);
			} else {
				entitiesToUpdate.add(entity);
			}
		}
		newEntities.forEach(entityManager::persist);
		entitiesToUpdate.forEach(entityManager::merge);
		Collection<Long> ids = entities.stream().map(Entity::getId).toList();
		CriteriaDelete<?> delete = entityManager.getCriteriaBuilder().createCriteriaDelete(entityClass);
		delete.where(delete.from(entityClass).get("id").in(ids).not());
		entityManager.createQuery(delete).executeUpdate();
	}
}
