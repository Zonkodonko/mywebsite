import {LocalizedText} from '../../shared/translation/LocalizedText';

export type TopicRaw = {
  id: string,
  image: any,
  title: LocalizedText,
  description: LocalizedText,
  lastChange?: number
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

export type BlogArticleRaw = {
  id?: number,
  image?: any,
  title: LocalizedText,
  content: LocalizedText,
  lastChange?: number,
  created: number,
  appearanceSettings: {
    imagePosition: string,
    titleImage?: any
  }
}

export type NewArticle = ArticleCreationData & {
  topic: string
}

export type EditArticle = ArticleCreationData & {
  id: number
  imagesToDelete?: string[]
}

export interface BlogArticle {
  id: number,
  image?: any,
  title: string,
  content: string,
  lastChange?: number,
  appearanceSettings: {
    imagePosition: string,
    titleImage?: any
  }
}

/**
 * Date to provide from user when creating a new article.
 */
export type ArticleCreationData = {
  title: LocalizedText,
  content: LocalizedText,
  images: File[]
  appearanceSettings: {
    imagePosition: imagePosition,
  }
}

export type imagePosition = 'LEFT' | 'RIGHT' | 'TOP'
