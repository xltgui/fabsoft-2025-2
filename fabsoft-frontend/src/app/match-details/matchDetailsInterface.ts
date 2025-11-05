import { Player } from "./playerInterface";

export interface MatchDetailsInterface {
  id: number;
  adminId: number;
  date: string; // yyyy-MM-dd
  startTime: string; // Data e hora completas (incluindo a data)
  endTime: string;   // Data e hora completas
  place: string;     // Assumindo que o enum 'SoccerPlace' é serializado como string
  matchCode: string;
  adminNickname: string;
  soccerPlayers: Player[];
    
  // Adicione campos que você precisará na tela, mas que não estão no DTO
  // Ex: dono da sala, pixKey. Você deve obter isso no backend ou através de outra chamada.
  qrCodeUrl?: string;
  isOwner?: boolean;
}