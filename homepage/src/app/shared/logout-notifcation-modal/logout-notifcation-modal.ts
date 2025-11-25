import {Component} from '@angular/core';
import {NgbActiveModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-logout-notifcation-modal',
  standalone: false,
  templateUrl: './logout-notifcation-modal.html',
  styleUrl: './logout-notifcation-modal.scss'
})
export class LogoutNotifcationModal {


  constructor(private modal: NgbActiveModal) {
  }

  refresh() {
    this.modal.close("refresh");
  }

  logout() {
    this.modal.close("logout");
  }
}
