import { Routes } from '@angular/router';
import { Register } from './register/register';
import { Login } from './login/login';
import { Lobby } from './lobby/lobby';
import { Confirmation } from './confirmation/confirmation';

export const routes: Routes = [
    {path: 'users/register', component: Register},
    {path: 'users/register/confirmation-success', component: Confirmation},
    {path: 'users/login', component: Login},
    {path: 'lobby', component: Lobby},
];
