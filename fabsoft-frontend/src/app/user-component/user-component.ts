import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { UserService } from '../service/user-service';
import { MatCardModule } from '@angular/material/card';
import { MatFormField, MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatButtonModule, MatIconButton } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-user-component',
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
  templateUrl: './user-component.html',
  styleUrl: './user-component.scss'
})
export class UserComponent implements OnInit{ 
  username: string = "";
  password: string = "";

  isButtonDisabled: boolean = true;


  constructor(
    private userService: UserService,
    private snackBar: MatSnackBar
  ) { }

  ngOnInit(): void {
    this.checkFormValidity();
  }

  registerUser() {
    const request = {
      username: this.username,
      password: this.password
    };

    
    this.userService.register(request).subscribe(
      (response) => {
      },
      (errorMessage) => {
        this.snackBar.open(errorMessage, 'Close', {
          duration: 5000, // fecha após 5 seg
          verticalPosition: 'top',
          horizontalPosition: 'center',
        });
      }
    );
  }

  checkFormValidity() {
    this.isButtonDisabled = !(
      this.username &&
      this.password
    );
  }

}
