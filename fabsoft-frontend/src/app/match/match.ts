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
  date: any = '';
  startTime: any = '';
  endTime: any = '';
  place: string = '';

  loading: boolean = false;

  constructor(
    private router: Router,
    private matchService: MatchService,
    private datePipe: DatePipe,
    private snackBar: MatSnackBar
  ){}

  onSubmit(){
    this.snackBar.dismiss();

    const datePart = this.datePipe.transform(this.date, 'yyyy-MM-dd');
    const timePartStart = this.datePipe.transform(this.startTime, 'HH:mm'); 
    const timePartEnd = this.datePipe.transform(this.endTime, 'HH:mm');
    
    if (!datePart || !timePartStart || !timePartEnd || !this.place) {
      this.showSnackbar('Por favor, preencha todos os campos.', 'Fechar');
      this.loading = false;
      return;
    }

    this.loading = true;

    const request = {
      date: datePart,
      startTime: `${datePart}T${timePartStart}:00`,
      endTime: `${datePart}T${timePartEnd}:00`,
      place: this.place
    };

    this.matchService.create(request).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['match', response.matchCode]);
      },
      error: (errorMessage) => {
        this.loading = false;
        this.showSnackbar(errorMessage, 'Fechar');
      }
    });  
  }

  onCancel(){
    this.router.navigate(['lobby'])
  }


  showSnackbar(message: string, action: string, duration: number = 4000){
    this.snackBar.open(message, action, {
      duration: duration,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

}
