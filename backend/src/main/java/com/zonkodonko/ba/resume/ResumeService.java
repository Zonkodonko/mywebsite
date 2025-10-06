package com.zonkodonko.ba.resume;

import com.zonkodonko.ba.resume.data.CareerStep;
import com.zonkodonko.ba.resume.data.Skill;

import java.util.Collection;

public interface ResumeService {

	Resume getResume();

	void updateSkills(Collection<Skill> skills);

	void updateCareer(Collection<CareerStep> skills);

	void updateAboutMe(String lang, String aboutMe);
}
