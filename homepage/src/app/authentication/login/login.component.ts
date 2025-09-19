import {Component, OnInit} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import {AuthenticationService} from '../authentication.service';
import {NgbActiveModal, NgbModal} from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'app-login',
  standalone: false,
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {

  public loginForm: FormGroup = new FormGroup({
    username: new FormControl('', {validators: Validators.required}),
    password: new FormControl('',{validators: Validators.required}),
  });


  constructor(private authService: AuthenticationService, private modal: NgbActiveModal) {
  }



  login() {
    this.authService
      .login(this.loginForm.value.password, this.loginForm.value.username)
      .subscribe(() => this.modal.close())
  }
}
