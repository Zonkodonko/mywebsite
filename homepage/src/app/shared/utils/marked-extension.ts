import {Renderer, Tokens} from 'marked';
import environment from '../../../environment';

export function getArticleRenderer(id: number, lastChange: number) {
  return {
    image({href, title, text, tokens}: Tokens.Image): string {
      let styleAttachment: string = ""
      if(text.endsWith('>')) {
        styleAttachment+="float:right;"
      }
      if(text.startsWith('<')) {
        styleAttachment+="float:left;"
      }
      if(text.includes('style=|')) {
        styleAttachment+=text.split('style=|')[1].split('|')[0];
      }
      //todo add more styles
      return `<img src="${environment.backendUrl}/images/article/${id}/${href}?time=${lastChange}" alt="${text}" title="${title}" class="img-fluid" style="max-width:100%;${styleAttachment}"/> `;
    },
    html({text}: Tokens.HTML | Tokens.Tag): string {
      return text;
    }
  }
}
