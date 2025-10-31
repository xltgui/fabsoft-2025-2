import { Routes } from '@angular/router';
import { Register } from './register/register';
import { Login } from './login/login';
import { Lobby } from './lobby/lobby';
import { Confirmation } from './confirmation/confirmation';
import { Match } from './match/match';
import { MatchDetails } from './match-details/match-details';

export const routes: Routes = [
    {path: '', redirectTo: 'users/login', pathMatch: 'full'},

    {path: 'users/register', component: Register},
    {path: 'users/register/confirmation-success', component: Confirmation},
    {path: 'users/login', component: Login},
    {path: 'lobby', component: Lobby},
    {path: 'match/create', component: Match},
    {path: 'match/:matchCode', component: MatchDetails},
    {path: 'match/showAll', component: Match},
    {path: 'match/find', component: Match}
];
