package com.zonkodonko.ba.resume.data;

import jakarta.persistence.*;
import jakarta.persistence.Entity;

import java.util.Set;

/**
 * Step in career.
 *
 * @author Z0nko
 * @version 13.08.2025
 */
@Entity
@Table(name = "CAREER_STEP")
public class CareerStep implements com.zonkodonko.ba.resume.data.Entity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String title;
    private Set<String> description;
	@Column(name = "date_from")
    private Long from;
	@Column(name = "date_to")
    private Long to;
    private String language;

    public CareerStep() {
    }

    public CareerStep(Long id, String title, Set<String> description, Long from, Long to, String language) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.from = from;
        this.to = to;
        this.language = language;
    }

	@Override
	public Long getId() {
		return id;
	}
}
