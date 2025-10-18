import { Component } from '@angular/core';
import { MaterialSharedModule } from '../material-shared-module';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmation',
  imports: [MaterialSharedModule],
  templateUrl: './confirmation.html',
  styleUrl: './confirmation.scss'
})
export class Confirmation {

  constructor(
    private router: Router
  ){}

  navigateToLogin(){
    this.router.navigate(['users/login']);
  }
}
