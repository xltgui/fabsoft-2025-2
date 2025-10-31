import { Component, OnInit } from '@angular/core';
import { MatchDetailsInterface } from './matchDetailsInterface';
import { ActivatedRoute, Router } from '@angular/router';
import { Player } from './playerInterface';
import { MatchService } from '../service/match-service';
import { MatchDetailsService } from '../service/match-details-service';
import { UserService } from '../service/user-service';
import { MaterialSharedModule } from '../material-shared-module';
import { MatCardModule } from '@angular/material/card';
import { CommonModule } from '@angular/common';
import { PixSetDetails } from './pixSetInterface';
import { NgForm } from '@angular/forms';

@Component({
  selector: 'app-match-details',
  imports: [
    MaterialSharedModule,
    CommonModule
  ],
  templateUrl: './match-details.html',
  styleUrl: './match-details.scss'
})
export class MatchDetails implements OnInit{
  matchData: MatchDetailsInterface | null = null;
  pixDetails: PixSetDetails | null = null
  matchCode: string = '';
  loading = true;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private matchService: MatchDetailsService,
    private userService: UserService
  ){}

ngOnInit(): void {
  this.matchCode = this.route.snapshot.paramMap.get('matchCode') || '';

  if(this.matchCode){
    this.loadMatchDetails();
  } else {
    this.router.navigate(['lobby']);
  }
}

loadMatchDetails() {
  this.matchService.getMatchDetails(this.matchCode).subscribe({
      next: (data) => {
          // 2. Mapeia a resposta do backend para o modelo do componente
          this.matchData = {
              ...data,
              // Adicione campos do frontend que não estão no DTO do backend:
              ownerName: 'Dono Fictício', // Você deve obter este nome do backend
              isOwner: this.checkIfOwner(data)
          };
          this.loading = false;
      },
      error: (err) => {
          console.error('Erro ao carregar partida:', err);
          this.loading = false;
          // Redirecionar em caso de erro (ex: partida não encontrada)
      }
  });
}

checkIfOwner(matchData: MatchDetailsInterface): boolean {
    // Lógica: Se o ID do usuário logado (userService.getCurrentUserId())
    // for igual ao ID do dono da partida (matchData.ownerId)
    // Isso requer que o backend envie o ID do dono.
    return true; // Simplificado por enquanto
}
    
    // ... (Seus métodos goToLobby, leaveMatch, copyCode...)

togglePaymentStatus(playerId: number) {
    if (!this.matchData || !this.matchData.isOwner) return;

    this.matchService.updatePaymentStatus(this.matchData.matchCode, playerId).subscribe({
        next: () => {
            // Atualiza a lista de jogadores após a chamada bem-sucedida
            this.loadMatchDetails(); 
        },
        error: (err) => console.error('Falha ao atualizar pagamento', err)
    });
}

setPixDetails(form: NgForm){}

copyCode() {
    alert('Código da sala copiado!');
}

leaveMatch() {
    // Lógica para sair da partida (chamar MatchService.leaveMatch)
}

goToLobby() {
    this.router.navigate(['/lobby']);
}

}
