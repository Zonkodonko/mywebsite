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

export type Topic = {
  id: string,
  image: any,
  title: string,
  description: string
}

export interface BlogArticleRaw {
  image?: any,
  title: LocalizedText,
  description: LocalizedText,
  date: number,
}

export interface BlogArticle {
  image?: any,
  title: string,
  description: string,
  date: number,
}
