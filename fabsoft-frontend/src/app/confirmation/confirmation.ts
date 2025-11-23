import { Component, OnInit } from '@angular/core';
import { MaterialSharedModule } from '../material-shared-module';
import { Router } from '@angular/router';

@Component({
  selector: 'app-confirmation',
  imports: [MaterialSharedModule],
  templateUrl: './confirmation.html',
  styleUrl: './confirmation.scss'
})
export class Confirmation implements OnInit{

  constructor(
    private router: Router
  ){}

  ngOnInit(): void {
    console.log("CONFIRMAÇÃO CARREGADA")
  }

  navigateToLogin(){
    this.router.navigate(['users/login']);
  }
}
