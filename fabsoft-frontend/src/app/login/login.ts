import { Component } from '@angular/core';
import { MaterialSharedModule } from '../material-shared-module';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { UserService } from '../service/user-service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-login',
  imports: [
    MaterialSharedModule,
    CommonModule
  ],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login {
  email: string = '';
  password: string = '';
  
  hidePassword: boolean = true; 
  loading: boolean = false;

  constructor(
    private router: Router,
    private userService: UserService,
    private snackBar: MatSnackBar
  ){}

  onSubmit(){
    this.snackBar.dismiss();

    if (!this.password || !this.email) {
      this.showSnackbar('Por favor, preencha todos os campos.', 'Fechar');
      return;
    }

    this.loading = true;

    const request = {
       email: this.email,
       password: this.password
    };

    this.userService.login(request).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['lobby'])
      },
      error: (errorMessage) => {
        this.loading = false;
        this.showSnackbar(errorMessage, 'Fechar');
      }
    });  
  
  }

  navigateToRegister(){
    this.router.navigate(['users/register']);
  }

  showSnackbar(message: string, action: string, duration: number = 4000){
    this.snackBar.open(message, action, {
      duration: duration,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  cleanFields(){
    this.email = '';
    this.password = '';
  }
}
