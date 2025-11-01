import { Component } from '@angular/core';
import { MaterialSharedModule } from '../material-shared-module';
import { Router } from '@angular/router';
import { MatchService } from '../service/match-service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-match-join',
  imports: [
    MaterialSharedModule,
    CommonModule
  ],
  templateUrl: './match-join.html',
  styleUrl: './match-join.scss'
})
export class MatchJoin {
  matchCode: string = '';
  loading: boolean = false;

  constructor(
    private router: Router,
    private matchService: MatchService,
    private snackBar: MatSnackBar
  ){}

  onSubmit(){
    this.snackBar.dismiss();

    if (!this.matchCode) {
      this.showSnackbar('Por favor, preencha o código da partida.', 'Fechar');
      this.loading = false;
      return;
    }

    this.loading = true;

    this.matchService.join(this.matchCode).subscribe({
      next: (response) => {
        this.loading = false;
        this.router.navigate(['match', this.matchCode]);
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
