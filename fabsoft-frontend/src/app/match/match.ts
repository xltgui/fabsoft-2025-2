import { Component, LOCALE_ID, NgModule } from '@angular/core';
import { Router } from '@angular/router';
import { MatchService } from '../service/match-service';
import { MaterialSharedModule } from '../material-shared-module';
import { CommonModule, DatePipe } from '@angular/common';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MAT_DATE_LOCALE, provideNativeDateAdapter } from '@angular/material/core';

@Component({
  selector: 'app-match',
  imports: [
    MaterialSharedModule,
    CommonModule,
],
providers: [
    DatePipe,
    provideNativeDateAdapter(),
    { provide: LOCALE_ID, useValue: 'pt-BR'},
    { provide: MAT_DATE_LOCALE, useValue: 'pt-BR'}
],
  templateUrl: './match.html',
  styleUrl: './match.scss'
})
export class Match {
  date: string = '';
  startTime: string = '';
  endTime: string = '';
  soccerPlace: string = '';

  loading: boolean = false;

  constructor(
    private router: Router,
    private matchService: MatchService,
    private snackBar: MatSnackBar
  ){}

  onSubmit(){
    this.snackBar.dismiss();
    
    if (!this.date || !this.startTime || !this.endTime || !this.soccerPlace) {
      this.showSnackbar('Por favor, preencha todos os campos.', 'Fechar');
      return;
    }

    this.loading = true;

    const request = {
      date: this.date,
      startTime: this.startTime,
      endTime: this.endTime,
      soccerPlace: this.soccerPlace
    };

    this.matchService.create(request).subscribe({
      next: (response) => {
        this.loading = false;
      },
      error: (errorMessage) => {
        this.loading = false;
        this.showSnackbar(errorMessage, 'Fechar');
      }
    });  
  }

  onCancel(){}


  showSnackbar(message: string, action: string, duration: number = 4000){
    this.snackBar.open(message, action, {
      duration: duration,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

}
