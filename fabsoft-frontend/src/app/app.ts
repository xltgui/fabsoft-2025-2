import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { Register } from './register/register';
import { Login } from './login/login';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected title = 'fabsoft-frontend-aux';
}
