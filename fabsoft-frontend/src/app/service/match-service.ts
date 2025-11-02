import { Injectable } from '@angular/core';
import { MatchModel } from '../model/match-model';
import { HttpClient, HttpParams } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { MatchItem } from '../lobby/MatchItem';

@Injectable({
  providedIn: 'root'
})
export class MatchService {
  apiURL = 'http://localhost:8080/match'

  constructor(private http:HttpClient){

  }

  create(request: any){
    return this.http.post<MatchModel>(this.apiURL, request).pipe(
      catchError(error => {
        const message = error.error?.message || 'Validation Error';
        return throwError( () => message);
      })
    );
  }

  getMatchList(): Observable<MatchItem[]>{
    return this.http.get<MatchItem[]>(`${this.apiURL}/byUser`);
  }

  join(matchCode: string){
    let params = new HttpParams();
    params = params.set('matchCode', matchCode);

    return this.http.get(`${this.apiURL}/join`, {params : params}).pipe(
      catchError(error => {
        const message = error.error?.message || 'Validation Error';
        return throwError( () => message);
      })
    );
  }
}
