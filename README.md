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
    User "*" --> "*" SoccerMatch
    namespace entity {
      class User{
          -id : Long
          -nome : String
          -telefone : String
          -email : String

          +getId():long
          +setId(id:long):void
          +getNome():String
          +setNome(nome:String):void
          +getTelefone():String
          +setTelefone(telefone:String):void
          +getEmail():String
          +setEmail(email:String):void
      }
      class SoccerMatch{
          -id : Long
          -matchId : Long
          -paymentKey : String
          -admin : User
          -players : List<String>

          +getId():Long
          +setId(id:Long):void
          +getMatchId():String
          +setMatchId(matchId:String):void
          +getPaymentKey():String
          +setPaymentKey(paymentKey:String):void
          +getAdmin():String
          +setAdmin(admin:String):void
          +getPlayers():String
          +setPlayers(players:String):void

      }
    }
   

