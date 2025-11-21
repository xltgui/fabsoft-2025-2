export const environment = {
  production: false,
  apiUrl: determineApiUrl()
};

function determineApiUrl(): string {
  const currentHost = window.location.hostname;
  
  // Se estiver rodando no GitHub Codespace
  if (currentHost.includes('app.github.dev')) {
    console.log("INCLUDES=" + currentHost)
    // Substitui a porta do frontend (4200) pela do backend (8080)
    return currentHost.replace('-4200.', '-8080.');
  }
  
  // Desenvolvimento local
  return 'http://localhost:8080';
}