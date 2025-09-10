package com.zonkodonko.ba.resume;

import com.zonkodonko.ba.resume.data.CareerStep;
import com.zonkodonko.ba.resume.data.Entity;
import com.zonkodonko.ba.resume.data.Skill;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaDelete;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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


	@Transactional(readOnly = true)
	@Override
	public Resume getResume() {
		Collection<Skill> skills = entityManager.createQuery("SELECT s FROM Skill s", Skill.class).getResultList();
		Resume.Builder builder = Resume.builder();
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
