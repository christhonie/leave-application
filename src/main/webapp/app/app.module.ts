import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { LeaveApplicationSharedModule } from 'app/shared/shared.module';
import { LeaveApplicationCoreModule } from 'app/core/core.module';
import { LeaveApplicationAppRoutingModule } from './app-routing.module';
import { LeaveApplicationHomeModule } from './home/home.module';
import { LeaveApplicationEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ActiveMenuDirective } from './layouts/navbar/active-menu.directive';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    LeaveApplicationSharedModule,
    LeaveApplicationCoreModule,
    LeaveApplicationHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    LeaveApplicationEntityModule,
    LeaveApplicationAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, ActiveMenuDirective, FooterComponent],
  bootstrap: [MainComponent],
})
export class LeaveApplicationAppModule {}
