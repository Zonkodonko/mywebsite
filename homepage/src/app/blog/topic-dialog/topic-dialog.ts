import {Component, ElementRef, ViewChild} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';
import {TopicRaw} from '../data/BlogTypes';
import {ImageService} from '../../shared/image-service/image-service';

@Component({
  selector: 'app-topic-dialog',
  standalone: false,
  templateUrl: './topic-dialog.html',
  styleUrl: './topic-dialog.scss'
})
export class TopicDialog {

  public titleLang: "de" | "en" = "de";
  public descriptionLang: "de" | "en" = "de";
  public imagePreview?: string;

  @ViewChild('fileInput', {static: false})
  fileInput?: ElementRef;

  public topicForm: FormGroup = new FormGroup({
    id: new FormControl("", [Validators.required]),
    title: new FormGroup({
      de: new FormControl("", [Validators.minLength(1), Validators.required]),
      en: new FormControl("", [Validators.minLength(1), Validators.required])
    }),
    description: new FormGroup({
      de: new FormControl("", [Validators.minLength(1), Validators.required]),
      en: new FormControl("", [Validators.minLength(1), Validators.required])
    }),
    image: new FormControl(null, Validators.required)
  });

  public image: File | null = null;

  constructor(private modal: NgbActiveModal,
              private imageService: ImageService
  ) {
  }


  public setData(topic: TopicRaw) {
    this.topicForm.patchValue(topic);
    if (topic.image) {
      this.imageService.getImageFor(topic.id).subscribe((img) => {
        const imgFile = new File([img.fileData], img.filename);
        this.image = imgFile;
        this.topicForm.controls["image"].setValue(imgFile);
        this.imagePreview = URL.createObjectURL(imgFile);
      })
    }
  }

  get titleController(): FormControl {
    return this.getTitleController(this.titleLang);
  }

  get descriptionController(): FormControl {
    return this.getDescriptionController(this.descriptionLang);
  }

  public getDescriptionController(lang: string): FormControl {
    return (this.topicForm.controls["description"] as FormGroup).controls[lang] as FormControl;
  }

  public getTitleController(lang: string): FormControl {
    return (this.topicForm.controls["title"] as FormGroup).controls[lang] as FormControl;
  }

  get idControl(): FormControl {
    return this.topicForm.controls["id"] as FormControl;
  }

  onSelectFile(event: any) {
    this.image = event.target.files[0];
    this.topicForm.controls["image"].setValue(this.image);
    const fileReader = new FileReader();
    fileReader.onload = (() => {
      this.imagePreview = fileReader.result as string;
    })
    fileReader.readAsDataURL(this.image!);
  }

  save(): void {
    this.modal.close(this.topicForm.getRawValue());
  }

  cancel(): void {
    this.modal.dismiss();
  }

}
