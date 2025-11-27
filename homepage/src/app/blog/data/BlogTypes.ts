import {LocalizedText} from '../../shared/translation/LocalizedText';

export type TopicRaw = {
  id: string,
  image: any,
  title: LocalizedText,
  description: LocalizedText
}

export type NewTopic = {
  id: string,
  image: File,
  title: LocalizedText,
  description: LocalizedText
}

export type FullTopic = {
  topic: TopicRaw,
  articles: BlogArticleRaw[]
}

export type Topic = {
  id: string,
  image: any,
  title: string,
  description: string
}

export interface BlogArticleRaw {
  image?: any,
  title: LocalizedText,
  content: LocalizedText,
  date: number,
}

export interface BlogArticle {
  image?: any,
  title: string,
  content: string,
  date: number,
}
