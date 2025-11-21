// base-url.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';

export const baseUrlInterceptor: HttpInterceptorFn = (req, next) => {
  const baseUrl = determineBaseUrl();
  
  // Se a URL já é absoluta (começa com http), não modifica
  if (req.url.startsWith('http')) {
    return next(req);
  }

  // Adiciona a base URL para URLs relativas
  const absoluteReq = req.clone({
    url: `${baseUrl}${req.url}`
  });

  return next(absoluteReq);
};

function determineBaseUrl(): string {
  const currentHost = window.location.hostname;
  
  if (currentHost.includes('app.github.dev')) {
    const backendUrl = currentHost.replace('-4200', '-8080');
    return `https://${backendUrl}`;
  }
  
  return 'http://localhost:8080';
}