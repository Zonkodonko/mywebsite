package com.zonkodonko.ba.resume.data;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.util.Objects;

/**
 * Hard skill in resume.
 */
@Entity
@Table(name = "SKILLS")
public final class Skill implements com.zonkodonko.ba.storage.Entity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Nullable
    private Long id;
    private String name;
    private Byte level;
	@Nullable
    private String description;
    private String category;

    public Skill() {
    }

    public Skill(Long id, String name, Byte level, String description, String category) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.description = description;
        this.category = category;
    }

	@Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

	public Byte getLevel() {
		return level;
	}

	public String getDescription() {
		return description;
	}

	public String getCategory() {
		return category;
	}


	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private Long id;
		private String name;
		private Byte level;
		private String description;
		private String category;

		public Builder setId(Long id) {
			this.id = id;
			return this;
		}

		public Builder setName(String name) {
			this.name = name;
			return this;
		}

		public Builder setLevel(Byte level) {
			this.level = level;
			return this;
		}

		public Builder setDescription(String description) {
			this.description = description;
			return this;
		}

		public Builder setCategory(String category) {
			this.category = category;
			return this;
		}

		public Skill build() {
			return new Skill(id, name, level, description, category);
		}
	}

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Skill) obj;
        return Objects.equals(this.name, that.name) &&
                Objects.equals(this.level, that.level) &&
                Objects.equals(this.description, that.description) &&
                Objects.equals(this.category, that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, level, description, category);
    }

    @Override
    public String toString() {
        return "Skill[" +
                "name=" + name + ", " +
                "level=" + level + ", " +
                "description=" + description + ", " +
                "category=" + category + ']';
    }
	
	

}
