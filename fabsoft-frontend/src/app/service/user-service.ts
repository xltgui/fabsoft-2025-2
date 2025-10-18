import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserModel } from '../model/user-model';
import { BehaviorSubject, catchError, Observable, tap, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  apiURL = 'http://localhost:8080/users'
  
  private currentUserSubject = new BehaviorSubject<UserModel | null>(null);
  public currentUser$ = this.currentUserSubject.asObservable();

  constructor(private http:HttpClient) {
    const storedUser = localStorage.getItem('currentUser');
    if(storedUser){
      this.currentUserSubject.next(JSON.parse(storedUser));
    }
  }

  login(credentials: any): Observable<any>{
    return this.http.post<UserModel>(this.apiURL + '/login', credentials).pipe(
      tap((response) => {
        this.currentUserSubject.next(response);

        localStorage.setItem('currentUser', JSON.stringify(response));
      }),
      catchError(error => {
        const message = error.error?.message || 'Validation Error';
        return throwError( () => message);
      })
    );
  }

  register(request: any){
    return this.http.post<UserModel>(this.apiURL + '/register', request).pipe(
      catchError(error => {
        const message = error.error?.message || 'Validation Error';
        return throwError( () => message);
      })
    );
  }

  confirmRegistration(token: string) {
    // O endpoint é GET e espera o token como query parameter
    // O responseType: 'text' é crucial, pois o backend retorna apenas uma string de sucesso
    return this.http.get(`${this.apiURL}/confirm?token=${token}`, { responseType: 'text' }).pipe(
      catchError(error => {
        // O backend retorna 400 Bad Request com a mensagem no corpo
        const message = error.error || 'Erro desconhecido na confirmação.'; 
        return throwError(() => message);
      })
    );
  }


  getUsername(): string {
    return this.currentUserSubject.value?.username || 'Jogador Desconhecido'
  }

  logout(): void {
    this.currentUserSubject.next(null);
    localStorage.removeItem('currentUser');
  }
}
