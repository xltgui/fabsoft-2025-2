import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserModel } from '../model/user-model';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';
import { C } from '@angular/cdk/keycodes';
import { environment } from '../environments/environment';

const TOKEN_KEY = 'jwtToken';
const CURRENT_USER_KEY = "currentUser";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  usersUrl = `/users`;
  authUrl = `/auth`;
  
  private currentUserSubject = new BehaviorSubject<UserModel | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http:HttpClient) {
    const storedUser = localStorage.getItem('currentUser');
    if(storedUser){
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  login(credentials:any): Observable<any>{
    return this.http.post<any>(this.authUrl + '/login', credentials)
    .pipe(
      tap((response) => {
        localStorage.setItem(TOKEN_KEY, response.token);

        if (response.user) {
          localStorage.setItem(CURRENT_USER_KEY, JSON.stringify(response.user));
          
          // 3. Atualiza o BehaviorSubject para notificar o app
          this.currentUserSubject.next(response.user);
        }
      }),
      catchError(error => {
        const message = error.error?.message || 'Validation Error';
        return throwError( () => message);
      })
    );
  }

  public getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  }

  register(request: any){
    return this.http.post<UserModel>(this.usersUrl + '/register', request).pipe(
      catchError(error => {
        const message = error.error?.message || 'Validation Error';
        return throwError( () => message);
      })
    );
  }

  confirmRegistration(token: string) {
    // O endpoint é GET e espera o token como query parameter
    // O responseType: 'text' é crucial, pois o backend retorna apenas uma string de sucesso
    return this.http.get(`${this.usersUrl}/confirm?token=${token}`, { responseType: 'text' }).pipe(
      catchError(error => {
        // O backend retorna 400 Bad Request com a mensagem no corpo
        const message = error.error || 'Erro desconhecido na confirmação.'; 
        return throwError(() => message);
      })
    );
  }


  getNickname(): string {
    return this.currentUserSubject.value?.nickname || 'Jogador Desconhecido'
  }

  getCurrentUserId(): number{
    return this.currentUserSubject.value?.id || 0
  }

  public cleanUpAuth(): void {
    localStorage.removeItem(TOKEN_KEY); 
    localStorage.removeItem(CURRENT_USER_KEY); 
    this.currentUserSubject.next(null);
  }

  logout(): void {
    this.cleanUpAuth();
  }
}
