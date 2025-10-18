import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatCardModule } from '@angular/material/card';
import { MatFormField, MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule, MatIconButton } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { UserService } from '../service/user-service';
import { MatProgressSpinner, MatSpinner } from '@angular/material/progress-spinner';


@Component({
  selector: 'app-register',
  imports: [
    FormsModule,
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinner,
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './register.html',
  styleUrl: './register.scss'
})
export class Register {
  username: string = '';
  email: string = '';
  password: string = '';
  confirmPassword: string = '';
  
  hidePassword: boolean = true; 
  hideConfirmPassword: boolean = true;
  loading: boolean = false;

  constructor(
    private userService: UserService,
    private snackBar: MatSnackBar
  ) { }

  onSubmit() {
    this.snackBar.dismiss();

    if (this.password !== this.confirmPassword) {
      this.showSnackbar('As senhas não coincidem!', 'Fechar');
      return;
    }

    if (!this.username || !this.password || !this.email) {
      this.showSnackbar('Por favor, preencha todos os campos.', 'Fechar');
      return;
    }

    this.loading = true;

    const request = {
       username: this.username,
       email: this.email,
       password: this.password
    };

    this.userService.register(request).subscribe({
      next: (response) => {
        this.loading = false;
        this.showSnackbar('Cadastro realizado! Por favor, verifique seu e-mail para confirmar a conta.', 'OK', 8000);
        this.cleanFields();
      },
      error: (errorMessage) => {
        this.loading = false;
        this.showSnackbar(errorMessage, 'Fechar');
      }
    });
  }

  showSnackbar(message: string, action: string, duration: number = 4000){
    this.snackBar.open(message, action, {
      duration: duration,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

  cleanFields(){
    this.username = '';
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
  }

}
