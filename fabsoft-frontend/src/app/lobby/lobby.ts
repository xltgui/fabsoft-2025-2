import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MaterialSharedModule } from '../material-shared-module';
import { UserService } from '../service/user-service';
import { MatchService } from '../service/match-service';
import { CommonModule } from '@angular/common';
import { MatchItem } from './MatchItem';

@Component({
  selector: 'app-lobby',
  imports: [
    MaterialSharedModule,
    CommonModule
  ],
  templateUrl: './lobby.html',
  styleUrl: './lobby.scss'
})

export class Lobby implements OnInit {
  nickname: string= 'Carregando...';

  matchList: MatchItem[] = [];


  constructor(
    private router: Router,
    private userService: UserService,
    private matchService: MatchService
  ) { }

  ngOnInit(): void {
    this.nickname = this.userService.getNickname();

    this.loadMatchList();

  }


  loadMatchList() {

    this.matchService.getMatchList().subscribe({
        next: (data: MatchItem[]) => {
            this.matchList = data;
            
        },
        error: (err) => {
            console.error('Erro ao carregar partida:', err);
        }
    });
  }

  goToMatchDetails(matchCode: string){
    this.router.navigate(['match', matchCode]);
  }

  createMatch() {
    this.router.navigate(['match/create']);
  }

  joinMatch() {
    this.router.navigate(['match/join']);
  }

  logout() {
    this.userService.cleanUpAuth();
    this.router.navigate(['users/login']);
  }

}
