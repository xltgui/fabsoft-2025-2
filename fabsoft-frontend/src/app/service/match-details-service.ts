import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MatchDetailsInterface } from '../match-details/matchDetailsInterface';
import { PixSetDetails } from '../match-details/pixSetInterface';

@Injectable({
  providedIn: 'root'
})
export class MatchDetailsService {
  private apiUrl = '/match'

  constructor(private http: HttpClient){}

  getMatchDetails(matchCode: string): Observable<MatchDetailsInterface>{
    return this.http.get<MatchDetailsInterface>(`${this.apiUrl}/show/${matchCode}`);
  }

  setPixDetails(request: PixSetDetails, matchCode: string) {
    return this.http.post(`${this.apiUrl}/pixkey/${matchCode}`, request);
  }

  generateQrCode(amount: string, matchCode: string): Observable<any>{
    const requestBody = { amount: amount };
    return this.http.post(`${this.apiUrl}/generateQrCode/${matchCode}`, requestBody);
  }

  updatePaymentStatus(matchCode: string, playerId: number): Observable<any> {
    return this.http.patch(`${this.apiUrl}/updatePayment/${matchCode}/${playerId}`, null);
  }

  removePlayer(matchCode: string, playerId: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/removePlayer/${matchCode}/${playerId}`);
  }

  leaveMatch(matchCode: string){
    return this.http.delete(`${this.apiUrl}/leave/${matchCode}`);
  }
}
