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
  articles: ArticleWithoutContent[]
}

export type Topic = {
  id: string,
  image: any,
  title: string,
  description: string
}


export type ArticleWithoutContent = {
  id: number,
  title: LocalizedText,
  description: LocalizedText,
  lastChange: number,
  created: number,
  appearanceSettings: {
    imagePosition: ImagePosition,
    titleImage?: any
  }
  topic: string
}

/**
 * Article as it is stored in backend.
 */
export type BlogArticleRaw = {
  id: number,
  title: LocalizedText,
  content: LocalizedText,
  description: LocalizedText,
  lastChange: number,
  created: number,
  appearanceSettings: {
    imagePosition: ImagePosition,
    titleImage?: any
  }
  topic: string
}

export type NewArticle = ArticleCreationData & {
  topic: string
}

export type EditArticle = ArticleCreationData & {
  id: number
  imagesToDelete?: string[]
}

/**
 * Article how it is displayed in frontend.
 */
export interface BlogArticle {
  id: number,
  title: string,
  content: string,
  description: string,
  lastChange: number,
  appearanceSettings: {
    imagePosition: string,
    titleImage?: any
  }
}

/**
 * Article how it is displayed in frontend.
 */
export interface BlogArticleWithoutContent {
  id: number,
  title: string,
  description: string,
  lastChange: number,
  appearanceSettings: {
    imagePosition: ImagePosition,
    titleImage?: any
  }
}

/**
 * Date to provide from user when creating a new article.
 */
export type ArticleCreationData = {
  title: LocalizedText,
  content: LocalizedText,
  description: LocalizedText,
  images: File[]
  appearanceSettings: {
    imagePosition: ImagePosition,
    titleImage?: string
  }
}

export type ImagePosition = 'LEFT' | 'RIGHT' | 'TOP' | null
