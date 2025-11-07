import { Component, Inject } from '@angular/core';
import { MaterialSharedModule } from '../material-shared-module';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

export interface ConfirmationDialogData {
  playerNickname: string;
  action: string;
}

@Component({
  selector: 'app-confirmation-dialog-component',
  imports: [
    MaterialSharedModule
  ],
  templateUrl: './confirmation-dialog-component.html',
  styleUrl: './confirmation-dialog-component.scss'
})
export class ConfirmationDialogComponent {
  constructor(
    @Inject(MAT_DIALOG_DATA) public data: ConfirmationDialogData
  ) {}
}
