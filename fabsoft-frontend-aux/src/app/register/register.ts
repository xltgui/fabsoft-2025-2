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


@Component({
  selector: 'app-register',
  imports: [
    FormsModule,
    MatCardModule,
    MatInputModule,
    MatFormFieldModule,
    MatButtonModule,
    MatIconModule,
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

  constructor(
    private userService: UserService,
    private snackBar: MatSnackBar
  ) { }

  onSubmit() {
    if (this.password !== this.confirmPassword) {
      console.error('As senhas não coincidem!');
      // TODO: Usar MatSnackBar aqui para mostrar o erro
      return;
    }

    if (this.username && this.email && this.password) {
      console.log('Dados prontos para cadastro:');
      console.log('Usuário:', this.username);
      console.log('Email:', this.email);
      // NÃO FAÇA LOG DA SENHA EM AMBIENTES REAIS!
      console.log('Senhas válidas. Enviando para o servidor...');
      
      // Implemente a chamada ao seu serviço de cadastro (API) aqui.
    } else {
      console.warn('Por favor, preencha todos os campos.');
    }
  }

}
