import {Component, Input, OnInit} from '@angular/core';
import {AuthenticationService} from '../../authentication/authentication.service';
import {Skill} from '../data/ResumeData';
import {ResumeApiService} from '../services/resume-api-service';

@Component({
  selector: 'app-skill-overview',
  standalone: false,
  templateUrl: './skill-overview.component.html',
  styleUrl: './skill-overview.component.scss'
})
export class SkillOverviewComponent implements OnInit {

  static readonly MODE_KEY = "skillEditMode";

  @Input()
  public maxLevel!: number;

  public _skills!: Map<number,Skill>;

  private pendingSkills: Skill[] = [];

  public collapsedCategories: string[] = [];

  public isEditMode: boolean = false;

  private originalSkills: Skill[] = [];

  private categories: Map<string, { name: string, level: number, category: string }[]> = new Map();
  public sortedCategories: {cat: string, skills: Skill[]}[] = [
    {cat: 'programminglanguages', skills: []},
    {cat: 'backend', skills: []},
    {cat: 'web', skills: []},
    {cat: 'databases', skills: []},
    {cat: 'devtools', skills: []},
    {cat: 'os', skills: []},
    {cat: 'other', skills: []}
  ];

  public colorIterator = [
    'red',
    'purple',
    'blue',
    'orange',
    'green',
    'yellow',
    'pink',
    'brown',
  ]

  constructor(private authenticationService: AuthenticationService, private resumeService: ResumeApiService) {
  }

  getColor(index: number) {
    let i = index % this.colorIterator.length;
    return this.colorIterator[i];
  }

  ngOnInit(): void {
    this.isEditMode = sessionStorage.getItem(SkillOverviewComponent.MODE_KEY) == 'true';
  }

  @Input()
  public set skills(skills: Skill[]) {
    this.originalSkills = skills;
    if(skills.length > 0) {
      this._skills = new Map(skills.map(skill => [skill.id!, skill]));
      this.maxLevel = skills.sort((a, b) => a.level - b.level)[skills.length - 1].level!
      this.sortedCategories.forEach(entry => {
        this.categories.set(entry.cat, []);
        entry.skills = [];
      })
      this._skills.forEach(skill => {
        this.categories.get(skill.category)?.push(skill);
      })
      this.sortSkillsInCategories();
    }
  }
  /**
   * Transfer category data to sortedCategories.
   */
  private sortSkillsInCategories() {
    this.sortedCategories.forEach(entry => {
      entry.skills = this.categories.get(entry.cat)!;
    })
  }

  sortSkills(category: string): { name: string, level: number, category: string }[] {
    let sortedSkills = this.sortedCategories.find(c => c.cat === category)!.skills!
    if(!this.isEditMode) {
      sortedSkills = sortedSkills.sort((a, b) => {
        if(this.pendingSkills.includes(a)) {
          return 1;
        }
        if (this.pendingSkills.includes(b)) {
          return -1;
        }
        return b.level - a.level
      });
    }
    return sortedSkills;
  }

  toggleEditMode() {
    if(this.isEditMode) {
      const skills = this.collectSkillsToSave();
      this.resumeService.updateSkillset(skills).subscribe({
        complete: () => {
          this.isEditMode = !this.isEditMode
          sessionStorage.setItem(SkillOverviewComponent.MODE_KEY, this.isEditMode.toString());
          this.skills = skills;
        },
        error: (error) => {
          this.skills = this.originalSkills;
          this.isEditMode = !this.isEditMode
          sessionStorage.setItem(SkillOverviewComponent.MODE_KEY, this.isEditMode.toString());
        }
      });
    } else {
      this.isEditMode = !this.isEditMode;
      sessionStorage.setItem(SkillOverviewComponent.MODE_KEY, this.isEditMode.toString());
    }
  }

  get isLoggedIn() {
    return this.authenticationService.isLoggedIn;
  }

  collectSkillsToSave(): Skill[] {
    return [
      ...Array.from(this._skills.values()),
      ...this.pendingSkills.filter(s => s.name != null && s.name.trim().length > 0)
    ];
  }

  updateSkillLevel(skill: Skill, level: number ) {
    if(this.isEditMode) {
      if(skill.id == undefined) {
        this.pendingSkills.find(s => s.category === skill.category && s.name === skill.name)!.level = level;
      } else {
        this._skills.get(skill.id!)!.level = level;
      }
    }
  }

  deleteSkill(skill: Skill) {
    if(this.isEditMode) {
      if(skill.id == undefined) {
      this.pendingSkills.splice(this.pendingSkills.indexOf(skill), 1);
      } else {
        this._skills.delete(skill.id!);
      }
      const cat = this.sortedCategories.find(c => c.cat === skill.category)!;
      cat.skills.splice(cat.skills.indexOf(skill), 1);
    }
  }

  /**
   * Add pending skill for category
   * @param category of skill
   */
  addSkill(category: string) {
    const newSkill = {id: undefined, name: "", level: 0, category: category, description: ""};
    this.pendingSkills.push(newSkill);
    this.sortedCategories.find(c => c.cat === category)!.skills.push(newSkill);
  }



}
