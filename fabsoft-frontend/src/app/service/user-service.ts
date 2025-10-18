import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { UserModel } from '../model/user-model';
import { catchError, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  apiURL = 'http://localhost:8080/users'

  constructor(private http:HttpClient) {}

  register(request: any){
    return this.http.post<UserModel[]>(this.apiURL, request).pipe(
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
}
