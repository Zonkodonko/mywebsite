import {Component} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import {ArticleCreationData, BlogArticleRaw, NewArticle} from '../data/BlogTypes';
import {ImageService} from '../../shared/image-service/image-service';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {LocalizedText} from '../../shared/translation/LocalizedText';

@Component({
  selector: 'app-article-dialog',
  standalone: false,
  templateUrl: './article-dialog.html',
  styleUrl: './article-dialog.scss'
})
export class ArticleDialog {

  public titleImage?: {
    name: string,
    url: string
  };

  public titleImageUpload: File | null = null;

  public titleLang: "de" | "en" = "de";
  public contentLang: "de" | "en" = "de";


  form: FormGroup = new FormGroup({
    title: new FormGroup({
      de: new FormControl("",Validators.required),
      en: new FormControl("",Validators.required)
    }),
    content: new FormGroup({
      de: new FormControl("",Validators.required),
      en: new FormControl("",Validators.required)
    }),
    images: new FormArray([]),
    appearanceSettings: new FormGroup({
      imagePosition: new FormControl('LEFT', Validators.required),
      titleImage: new FormControl(""),
    }),
  });

  constructor(private imageService: ImageService, private modal: NgbActiveModal) {
  }

  set article(article: BlogArticleRaw) {
    this.form.patchValue(article);
    console.log(article);
    if(article.id != undefined) {
      this.imageService.getImageFor(article.id!, "article", article.lastChange).subscribe((img) => {
        this.titleImage = {
          name: img.filename,
          url: URL.createObjectURL(img.fileData)
        }
      })
    }
  }

  onSelectFile(event: any) {
    this.titleImageUpload = event.target.files[0];
    this.settingsControl.controls["titleImage"].setValue(this.titleImageUpload);
    this.titleImage = {
      name: this.titleImageUpload!.name,
      url: URL.createObjectURL(this.titleImageUpload!)
    }
  }

  getTitleControl(lang: string): FormControl {
    return (this.form.controls["title"] as FormGroup).controls[lang] as FormControl;
  }

  get titleControl(): FormControl {
    return this.getTitleControl(this.titleLang);
  }

  get settingsControl(): FormGroup {
    return this.form.controls["appearanceSettings"] as FormGroup;
  }

  getContentControl(lang: string): FormControl {
    return (this.form.controls["content"] as FormGroup).controls[lang] as FormControl;
  }

  get contentControl(): FormControl {
    return this.getContentControl(this.contentLang);
  }

  cancel(): void {
    this.modal.dismiss();
  }

  save(): void {
    const rawValue = this.form.getRawValue();
    const articleData: ArticleCreationData = {
      content: rawValue.content as LocalizedText,
      title: rawValue.title as LocalizedText,
      image: this.titleImageUpload!,
      appearanceSettings: {
        imagePosition: rawValue.appearanceSettings.imagePosition
      }
    }
    this.modal.close(articleData);
  }


}
