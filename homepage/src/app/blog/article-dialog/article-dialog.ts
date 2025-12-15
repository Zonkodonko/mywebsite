import {Component} from '@angular/core';
import {FormArray, FormControl, FormGroup, Validators} from '@angular/forms';
import {ArticleCreationData, BlogArticleRaw, EditArticle, NewArticle} from '../data/BlogTypes';
import {Image, ImageService} from '../../shared/image-service/image-service';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {LocalizedText} from '../../shared/translation/LocalizedText';
import {isStringEmpty} from '../../shared/utils/utils';

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

  private _images: Image[] = [];
  private _imageData: Map<string, string> = new Map();
  private _originalArticle?: BlogArticleRaw;

  public imagesToRemove: string[] = [];
  public imagesToOverwrite: string[] = [];
  public newImages: string[] = [];

  /**
   * Images to fall back to if file input changes and image should not be overwritten or deleted anymore
   */
  private legacyImages: Image[] = [];

  form: FormGroup = new FormGroup({
    title: new FormGroup({
      de: new FormControl("",Validators.required),
      en: new FormControl("",Validators.required)
    }),
    description: new FormGroup({
      de: new FormControl("",Validators.required),
      en: new FormControl("",Validators.required)
    }),
    content: new FormGroup({
      de: new FormControl("",Validators.required),
      en: new FormControl("",Validators.required)
    }),
    images: new FormControl(new Set<File>()),
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
      this.imageService.getAllImagesForArticle(article.id!, article.lastChange!).subscribe((remoteImages) => {
        this._images = remoteImages;
        if(!isStringEmpty(article.appearanceSettings?.titleImage)) {
          const foundTitleImg = this._images.find(img => img.filename === article.appearanceSettings.titleImage)!;
          this.titleImage = {
            name: foundTitleImg.filename,
            url: URL.createObjectURL(foundTitleImg.fileData)
          }
        }
        this._images.forEach(img => this._imageData.set(img.filename, URL.createObjectURL(img.fileData)));
      })
      this._originalArticle = article;
    }
  }

  onSelectFile(event: Event) {
    const uploadFiles = (event.target as HTMLInputElement).files!;
    for (let file of uploadFiles) {
      const fileName = file.name;
      const imageObject:Image = {
        filename: fileName,
        fileData: file
      }
      this._imageData.set(fileName, URL.createObjectURL(file));
      if(this._images.findIndex(img => img.filename == fileName) !== -1) { //file already exists
        if(this.imagesToRemove.includes(fileName)) { //file was removed before
          this.imagesToRemove.splice(this.imagesToRemove.indexOf(fileName), 1);
          this.imagesToOverwrite.push(fileName);

        } else if(this.newImages.includes(fileName) || this.imagesToOverwrite.includes(fileName)) { //file was added or overwritten before
          this._images[this._images.findIndex(img => img.filename == fileName)] = imageObject;

        } else { //filename already exists in legacy data
          const localIndex = this._images.findIndex(img => img.filename == fileName);
          this.imagesToOverwrite.push(fileName);
          this.legacyImages.push(Object.assign({},this._images[localIndex]));
          this._images[localIndex] = imageObject;
        }
      } else {
        this._images.push(imageObject);
        this.newImages.push(fileName);
      }
      this.imageController.setValue(new Set([...this.imageController.value.values(), file]));
    }

  }

  /**
   * Images that are not about to be deleted.
   */
  get validImages(): string[] {
    return this._images
      .map(img => img.filename)
      .filter(filename => !this.imagesToRemove.includes(filename))
      .sort((a, b) => a.localeCompare(b));
  }

  get images(): Image[] {
    return this._images.sort((a, b) => {
      if(this.newImages.includes(a.filename) && !this.newImages.includes(b.filename)) {
        return -1;
      } else if(!this.newImages.includes(a.filename) && this.newImages.includes(b.filename)) {
        return 1;
      } else if(this.imagesToRemove.includes(a.filename) && !this.imagesToRemove.includes(b.filename)) {
        return 1;
      } else if(!this.imagesToRemove.includes(a.filename) && this.imagesToRemove.includes(b.filename)) {
        return -1;
      } else {
        return a.filename.localeCompare(b.filename);
      }
    });
  }

  get descriptionControlGroup(): FormGroup {
    return this.form.controls["description"] as FormGroup;
  }

  get imageController(): FormControl<Set<File>> {
    return this.form.controls["images"] as FormControl;
  }

  isImageNew(filename: string): boolean {
    return this.newImages.includes(filename);
  }

  isImageRemoved(filename: string): boolean {
    return this.imagesToRemove.includes(filename);
  }

  isImageOverwritten(filename: string): boolean {
    return this.imagesToOverwrite.includes(filename);
  }

  getImageDataUrl(fileName: string): string {
    return this._imageData.get(fileName)!;
  }

  removeImage(filename: string) {
    let removeFromImages = false;
    if(this.isImageNew(filename)) {
      this.newImages.splice(this.newImages.indexOf(filename), 1);
      removeFromImages = true;
    } else if( this.isImageOverwritten(filename)) {
      this.imagesToOverwrite.splice(this.imagesToOverwrite.indexOf(filename), 1);
      this.imagesToRemove.push(filename);
    } else { //is old image
      this.legacyImages.push(Object.assign({},this._images.find(img => img.filename == filename)!));
      this.imagesToRemove.push(filename);
    }
    this.imageController.value.delete(this._images.find(img => img.filename == filename)!.fileData as File);
    if(removeFromImages) {
      this._images.splice(this._images.findIndex(img => img.filename == filename), 1);
    }
    if(this.settingsControl.controls["titleImage"].value == filename) {
      this.settingsControl.controls["titleImage"].setValue("");
    }
  }

  resetImage(fileName: string) {
    const index = this._images.findIndex(img => img.filename == fileName);
    const deleteSuccess = this.imageController.value.delete(this._images[index].fileData as File);
    console.log(`Deleted file ${fileName} from image controller: ${deleteSuccess}`)
    const legacyIndex = this.legacyImages.findIndex(img => img.filename == fileName);
    console.log(`Legacy index: ${legacyIndex}`)
    if(legacyIndex !== -1) {
      this._images[index] = Object.assign({},this.legacyImages[legacyIndex]);
      this.legacyImages.splice(legacyIndex, 1);
      if(this.imagesToRemove.includes(fileName)) {
        this.imagesToRemove.splice(this.imagesToRemove.indexOf(fileName), 1);
      }
      if(this.imagesToOverwrite.includes(fileName)) {
        this.imagesToOverwrite.splice(this.imagesToOverwrite.indexOf(fileName), 1);
      }
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

  get contentControlGroup() {
    return this.form.controls["content"] as FormGroup;
  }

  get titleControlGroup() {
    return this.form.controls["title"] as FormGroup;
  }

  get contentControl(): FormControl {
    return this.getContentControl(this.contentLang);
  }

  cancel(): void {
    this.modal.dismiss();
  }

  save(): void {
    const rawValue = this.form.getRawValue();
    let articleData: ArticleCreationData | EditArticle = {
      content: rawValue.content as LocalizedText,
      title: rawValue.title as LocalizedText,
      images: [...(rawValue.images as Set<File>).values()],
      description: rawValue.description as LocalizedText,
      appearanceSettings: rawValue.appearanceSettings,
    }
    if(this._originalArticle !== undefined) {
      articleData = Object.assign(articleData,{id: this._originalArticle.id} )
      if(this.imagesToRemove.length > 0) {
        articleData = Object.assign(articleData, {imagesToDelete: this.imagesToRemove});
      }
    }
    this.modal.close(articleData);
  }


}
