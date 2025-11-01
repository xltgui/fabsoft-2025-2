import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { MaterialSharedModule } from '../material-shared-module';
import { UserService } from '../service/user-service';

@Component({
  selector: 'app-lobby',
  imports: [MaterialSharedModule],
  templateUrl: './lobby.html',
  styleUrl: './lobby.scss'
})

export class Lobby implements OnInit {
  nickname: string= 'Carregando...';

  constructor(
    private router: Router,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.nickname = this.userService.getNickname();
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
