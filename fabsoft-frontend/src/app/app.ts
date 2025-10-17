import { Component, signal } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UserComponent } from './user-component/user-component';
import { Register } from './register/register';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, UserComponent, Register],
  templateUrl: './app.html',
  styleUrl: './app.scss'
})
export class App {
  protected readonly title = signal('fabsoft-frontend');
}
