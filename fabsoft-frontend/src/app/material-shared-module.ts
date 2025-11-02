import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

// 1. Imports do Angular Material que você usa
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

// 2. Imports do Angular para o formulário
import { FormsModule } from '@angular/forms'; 
import { MatSidenav, MatSidenavContainer, MatSidenavContent, MatSidenavModule } from '@angular/material/sidenav';
import { MatToolbar, MatToolbarModule } from '@angular/material/toolbar';
import { MatListItem, MatListModule, MatNavList } from '@angular/material/list';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatTimepickerModule } from '@angular/material/timepicker';
import { MatButtonToggleModule } from '@angular/material/button-toggle';

// Crie um array para facilitar a organização
const materialImports = [
  MatCardModule,
  MatInputModule,
  MatFormFieldModule,
  MatIconModule,
  MatButtonModule,
  MatProgressSpinnerModule,
  MatSidenavModule,     
  MatToolbarModule, 
  MatListModule,
  MatDatepickerModule,
  MatTimepickerModule,
  MatButtonToggleModule
  // Adicione todos os outros módulos do Material aqui
];

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    FormsModule, // Inclua módulos comuns que você usa com frequência
    ...materialImports,
  ],
  // 3. O SEGREDO: Exporte todos os módulos para que eles fiquem disponíveis
  //    para qualquer componente que importar este SharedModule
  exports: [
    FormsModule,
    ...materialImports,
  ]
})
export class MaterialSharedModule { }