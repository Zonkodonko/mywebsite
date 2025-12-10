import {Component, Input, TemplateRef, ViewChild} from '@angular/core';
import {FormControl, FormGroup} from '@angular/forms';

@Component({
  selector: 'app-multi-language-text-input',
  standalone: false,
  templateUrl: './multi-language-text-input.html',
  styleUrl: './multi-language-text-input.scss'
})
export class MultiLanguageTextInput {

  public selectedLang!: string;

  @ViewChild('componentWrapper')
  public componentWrapper!: TemplateRef<any>;

  @Input()
  public languages: string[] = ['de', 'en'];

  @Input()
  public formGroup!: FormGroup;

  @Input()
  public label!: string;

  @Input()
  public size: 'big'|'normal'|'small' = 'normal';

  _selectedControl!: FormControl;


  get selectedControl(): FormControl {
    return this.getFormControl(this.selectedLang) as FormControl;
  }

  getFormControl(lang: string) {
    return this.formGroup.get(lang)!;
  }

  @Input()
  public set selectedLanguage(lang: string) {
    this.selectedLang = lang;
    this._selectedControl = this.getFormControl(lang) as FormControl;
  }
}
