package com.zonkodonko.ba.resume;

import com.zonkodonko.ba.resume.data.CareerStep;
import com.zonkodonko.ba.resume.data.Skill;

import java.util.Collection;
import java.util.Objects;

public final class Resume {

	private final String name;
	private final Collection<Skill> skills;
	private final CareerStep[] career;
	private final String aboutMe;

	public Resume(String name, Collection<Skill> skills, CareerStep[] career, String aboutMe) {
		this.name = name;
		this.skills = skills;
		this.career = career;
		this.aboutMe = aboutMe;
	}

	public String getName() {
		return name;
	}

	public Collection<Skill> getSkills() {
		return skills;
	}

	public CareerStep[] getCareer() {
		return career;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (Resume) obj;
		return Objects.equals(this.name, that.name) &&
				Objects.equals(this.skills, that.skills) &&
				Objects.equals(this.career, that.career);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, skills, career);
	}

	@Override
	public String toString() {
		return "Resume[" +
				"name=" + name + ", " +
				"skills=" + skills + ", " +
				"career=" + career + ']';
	}


	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private String name;
		private Collection<Skill> skills;
		private CareerStep[] career;
		private String aboutMe;

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setSkills(Collection<Skill> skills) {
			this.skills = skills;
			return this;
		}

		public Builder setCareer(CareerStep[] career) {
			this.career = career;
			return this;
		}

		public Builder setAboutMe(String aboutMe) {
			this.aboutMe = aboutMe;
			return this;
		}

		public Resume build() {
			return new Resume(name, skills, career, aboutMe);
		}
	}
}


