import { Component } from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-confirm-dialog',
  standalone: false,
  templateUrl: './confirm-dialog.html',
  styleUrl: './confirm-dialog.scss'
})
export class ConfirmDialog {

  constructor(private modal: NgbActiveModal) {

  }

  confirm() {
    this.modal.close(true);
  }

  cancel() {
    this.modal.dismiss();
  }

}
