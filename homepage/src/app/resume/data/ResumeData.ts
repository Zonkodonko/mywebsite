export type ResumeData = {
    aboutMe: string,
    experience: Career,
    skills: Skill[]
}

export type Career = {
  id?: number,
  title: string,
  description: string[],
  from: number,
  to: number
}[]

export type Skill = {
  id?: number,
  name: string,
  level: number,
  category: string
  description?: string
}
