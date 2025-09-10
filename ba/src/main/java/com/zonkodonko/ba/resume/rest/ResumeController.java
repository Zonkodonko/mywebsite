package com.zonkodonko.ba.resume.rest;

import com.zonkodonko.ba.resume.Resume;
import com.zonkodonko.ba.resume.ResumeService;
import com.zonkodonko.ba.resume.data.Skill;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/resume")
class ResumeController {

	private final ResumeService resumeService;

	public ResumeController(ResumeService resumeService) {
		this.resumeService = resumeService;
	}

	@GetMapping
	public Resume getResume() {
		return resumeService.getResume();
	}

	@PostMapping("/skills")
	public void updateSkills(@RequestBody List<Skill> skills) {
		resumeService.updateSkills(skills);
	}

	@GetMapping("/skills")
	public Collection<Skill> getSkills() {
		return resumeService.getResume().skills();
	}
}
