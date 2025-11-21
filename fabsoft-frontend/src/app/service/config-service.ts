import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {
   private baseUrl = this.determineBaseUrl();

  getBaseUrl(): string {
    return this.baseUrl;
  }

  private determineBaseUrl(): string {
    const currentHost = window.location.hostname;
    
    if (currentHost.includes('app.github.dev')) {
      const backendUrl = currentHost.replace('-4200', '-8080');
      return `https://${backendUrl}`;
    }
    
    return 'http://localhost:8080';
  }
}
