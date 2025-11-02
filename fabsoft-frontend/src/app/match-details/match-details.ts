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
      const loggedUserId = this.userService.getCurrentUserId(); 
      return loggedUserId === matchData.adminId
  }
    
  togglePaymentStatus(playerId: number) {
      if (!this.matchData || !this.matchData.isOwner) return;

      const player = this.matchData.soccerPlayers.find(p => p.id === playerId);
      if (player) {
          player.paid = !player.paid; 
      }

      this.matchService.updatePaymentStatus(this.matchData.matchCode, playerId).subscribe({
          next: () => {
          },
            error: (err) =>{ console.error('Falha ao atualizar pagamento', err);
            if (player)  player.paid = !player?.paid;
          }
      });
  }


  getCurrentUserId(): number {
    return this.userService.getCurrentUserId(); 
  }

  removePlayer(playerId: number) {
    if (confirm('Tem certeza que deseja expulsar este jogador?')) {
        // TODO: Implementar a chamada ao MatchService.removePlayer(matchCode, playerId)
        console.log(`Expulsando o jogador ID: ${playerId} da partida ${this.matchCode}`);
        // Exemplo de chamada:
        this.matchService.removePlayer(this.matchCode, playerId).subscribe(() => {
            this.loadMatchDetails(); // Recarrega a lista após a remoção
        });
    }
  }

  trackByPlayerId(index: number, player: Player): number {
      return player.id; 
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
