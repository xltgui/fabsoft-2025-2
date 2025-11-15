import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatchDetailsInterface } from './matchDetailsInterface';
import { ActivatedRoute, Router } from '@angular/router';
import { Player } from './playerInterface';
import { MatchDetailsService } from '../service/match-details-service';
import { UserService } from '../service/user-service';
import { MaterialSharedModule } from '../material-shared-module';
import { CommonModule } from '@angular/common';
import { PixSetDetails } from './pixSetInterface';
import { NgForm } from '@angular/forms';
import { Clipboard } from '@angular/cdk/clipboard';
import { MatSnackBar } from '@angular/material/snack-bar';
import { KeyTypeOption } from './KeyTypeOption';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmationDialogComponent } from '../confirmation-dialog-component/confirmation-dialog-component';

@Component({
  selector: 'app-match-details',
  imports: [
    MaterialSharedModule,
    CommonModule
  ],
  templateUrl: './match-details.html',
  styleUrl: './match-details.scss'
})
export class MatchDetails implements OnInit, OnDestroy{
  matchData: MatchDetailsInterface | null = null;
  displayQrCodeUrl: string | null = null;

  amount: string = '';
  qrCodeUrl: string | null = null;
  matchCode: string = '';
  loading = true;
  loadingPix = false;
  loadingQrCode = false;
  isEditingPixDetails = false;

  keyTypesOptions: KeyTypeOption[] = [
  { value: 'PHONE', label: 'Telefone' },
  { value: 'CPF', label: 'CPF' },
  { value: 'EMAIL', label: 'Email' },
  { value: 'RANDOM', label: 'Aleatório' }
  ];

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private clipboard: Clipboard,
    private snackbar: MatSnackBar,
    private dialog: MatDialog,
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

  ngOnDestroy() {
        if (this.qrCodeUrl) {
            URL.revokeObjectURL(this.qrCodeUrl);
        }
    }

  loadMatchDetails() {
    this.matchService.getMatchDetails(this.matchCode).subscribe({
        next: (data) => {
            // 2. Mapeia a resposta do backend para o modelo do componente
            this.matchData = {
                ...data,
                isOwner: this.checkIfOwner(data)
            };

            this.isEditingPixDetails = !data.pixKeyDetails;

            if(this.matchData.qrCodeUrl){
              this.displayQrCodeUrl = 'data:image/png;base64,' + this.matchData.qrCodeUrl;
            } else {
              this.displayQrCodeUrl = null;
            }

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

  removePlayer(playerId: number, playerNickname: string) {
    const action = 'expulsar';
    const message = 'o jogador';
    const highlight = playerNickname;
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: {
        action: action,
        message: message,
        highlight: highlight
      }
    });


    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.matchService.removePlayer(this.matchCode, playerId).subscribe(() => {
            this.loadMatchDetails();
        });
      }
    });
  }

  trackByPlayerId(index: number, player: Player): number {
      return player.id; 
  }

  setPixDetails(form: NgForm){
    if(form.invalid || !this.matchCode) return;

    this.loadingPix = true;

    const request: PixSetDetails = form.value

    this.matchService.setPixDetails(request, this.matchCode).subscribe({
      next: () => {

        if(this.matchData){
          this.matchData = {
            ...this.matchData,
            pixKeyDetails: request,
          } as MatchDetailsInterface
        }

        this.isEditingPixDetails = false;
        this.loadingPix = false;
      },
      error: (err) => {
          this.loadingPix = false;
          console.error('Erro ao salvar chave PIX', err);
      }
    });
  }

  arePixFieldsFilled(): boolean {
  if (!this.matchData?.pixKeyDetails) {
    return false;
  }
  
  const pixDetails = this.matchData.pixKeyDetails;
  return !!(
    pixDetails.keyType &&
    pixDetails.keyValue && 
    pixDetails.recipientName &&
    pixDetails.recipientCity
  );
}

  enablePixEditing() {
        this.isEditingPixDetails = true;
    }
    
    // NOVO MÉTODO: Cancela a edição (volta ao modo visualização)
    cancelPixEditing() {
        this.loadMatchDetails(); 
        this.isEditingPixDetails = false;
    }

  generateQrCode(){
    if(!this.amount) return;

    this.loadingQrCode = true;


    this.matchService.generateQrCode(this.amount, this.matchCode).subscribe({
      next: () => {
        this.loadMatchDetails();
        this.loadingQrCode = false;
      },
      error: (err) => {
        this.loadingQrCode = false;
        console.error('Falha ao gerar QR Code', err);
      }
    });
  }

  copyCode() {
      if(this.matchData && this.matchData.matchCode){
        const success = this.clipboard.copy(this.matchData.matchCode);

        if(success){
          this.showSnackbar(`Código copiado!`, 'Fechar', 3000);
        } else {
          // Opcional: Lidar com falha na cópia (embora raro em navegadores modernos)
          console.error('Falha ao copiar o código para o clipboard.');
          this.showSnackbar('Falha ao copiar o código. Tente manualmente', 'Fechar', 3000);
        }
      }
  }

  copyPixKeyValue() {
      if(this.matchData && this.matchData.pixKeyDetails?.keyValue){
        const success = this.clipboard.copy(this.matchData.pixKeyDetails.keyValue);

        if(success){
          this.showSnackbar(`Chave copiada!`, 'Fechar', 3000);
        } else {
          // Opcional: Lidar com falha na cópia (embora raro em navegadores modernos)
          console.error('Falha ao copiar a chave para o clipboard.');
          this.showSnackbar('Falha ao copiar a chave. Tente manualmente', 'Fechar', 3000);
        }
      }
  }

  leaveMatch() {
    const action = 'sair';
    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '350px',
      data: {
        action: action
      }
    });


    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.matchService.leaveMatch(this.matchCode).subscribe({
          next: () => {
            this.goToLobby();
          },
          error: (err) => {
            this.loadingQrCode = false;
            console.error('Falha ao sair da partida', err);
          }
        });
      }
    });    
  }

  goToLobby() {
      this.router.navigate(['/lobby']);
  }


  showSnackbar(message: string, action: string, duration: number = 4000){
    this.snackbar.open(message, action, {
      duration: duration,
      horizontalPosition: 'center',
      verticalPosition: 'top'
    });
  }

}
