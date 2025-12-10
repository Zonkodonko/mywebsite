import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IntersectAnimation } from './intersect-animation/intersect-animation';
import { AutoHeight } from './auto-resize/auto-height.directive';
import { LogoutNotifcationModal } from './logout-notifcation-modal/logout-notifcation-modal';
import { ConfirmDialog } from './components/confirm-dialog/confirm-dialog';
import { MultiLanguageTextInput } from './components/multi-language-text-input/multi-language-text-input';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {TranslatePipe} from '@ngx-translate/core';
import { SlugifyPipe } from './pipes/slugify-pipe';



@NgModule({
  declarations: [
    IntersectAnimation,
    AutoHeight,
    LogoutNotifcationModal,
    ConfirmDialog,
    MultiLanguageTextInput,
    SlugifyPipe
  ],
  exports: [
    IntersectAnimation,
    AutoHeight,
    MultiLanguageTextInput,
    SlugifyPipe
  ],
  imports: [
    CommonModule,
    FormsModule,
    TranslatePipe,
    ReactiveFormsModule
  ]
})
export class SharedModule { }
