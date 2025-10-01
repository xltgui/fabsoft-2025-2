# Fábrica de software 2025/2

Guilherme Passos de Borba

## Sistema para controle de pagamentos de partidas de futebol
  * Cadastro de usuário
  * Cadastro de uma partida
  * Atualização de status de pagamento


## Aula 05/08

- JRE - Java Runtime Environment
    - Ambiente mínimo para executar um programa Java
    - JVM - Java Virtual Machine (java.exe ou javaw.exe)

- JDK - Java Devlopment Kit
    - Ambiente de desenvolvimento (javac.exe - compilador)

- Compilaçãot
  1) Escreve um programa em java (arquivo.java)
  2) Compilação -> arquivo.java -> javac.exe -> bytecode ou arquivo.class)
- Execução
  3) Passar .class para java.exe (JVM) -> linguagem de máquina


## Histórias de usuários

1) Eu como usuario, gostaria fazer meu cadastro no aplicativo.
2) Eu como usuario, gostaria de criar um sala para um partida de futebol.
3) Eu como usuario, gostaria de entrar em uma sala de uma partida de futebol existente.
4) Eu como usuario criador de uma sala, gostaria de poder cadastrar uma chave PIX na sala.
5) Eu como usuario criador de uma sala, gostaria de atualizar os status de pagamento na lista de todos os participantes da sala.

##

```mermaid
---
title: Diagrama de Entidades
---
classDiagram
    direction LR

    %% Relacionamentos (Associações)
    UserEntity "1" --> "*" SoccerMatch : Admin
    UserEntity "1" --> "*" SoccerPlayer : Jogador
    SoccerMatch "1" --> "1" PixKey : Chave Pix
    SoccerMatch "1" --> "*" SoccerPlayer : Participantes
    SoccerPlayer "*" --> "1" SoccerMatch : Partida

    %% Definição das Classes
    class UserEntity{
        -id : Long
        -username : String
        -email : String
        -password : String
    }

    class PixKey{
        -id : Long
        -keyType : KeyType (Enum)
        -keyValue : String
        -recipientName : String
        -recipientCity : String
    }

    class SoccerMatch{
        -id : Long
        -date : LocalDate
        -startTime : LocalTime
        -endTime : LocalTime
        -matchCode : String
        -paymentKey : String
        -payload : String
        -place : SoccerPlace (Enum)
        +pixKey : PixKey (OneToOne)
        +admin : UserEntity (ManyToOne)
        +soccerPlayers : List<SoccerPlayer> (OneToMany)
    }

    class SoccerPlayer{
        -id : Long
        -paid : Boolean
        +userEntity : UserEntity (ManyToOne)
        +match : SoccerMatch (ManyToOne)
    }
    
    
   

