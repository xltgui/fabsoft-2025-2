import { bootstrapApplication } from '@angular/platform-browser';
import { appConfig } from './app/app.config';
import { App } from './app/app';

import { registerLocaleData } from '@angular/common';
import pt from '@angular/common/locales/pt';
registerLocaleData(pt, 'pt-BR');

bootstrapApplication(App, appConfig)
  .catch((err) => console.error(err));
