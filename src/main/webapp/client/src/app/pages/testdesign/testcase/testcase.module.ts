import { NgModule }      from '@angular/core';
import { CommonModule }  from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { NgaModule } from '../../../theme/nga.module';

import { routing }       from './testcase.routing';

import { PaginationModule} from 'ng2-bootstrap';
import { TabsModule } from 'ng2-bootstrap';
import { CollapseModule } from 'ng2-bootstrap';
import { ModalModule } from 'ng2-bootstrap';
import { ButtonsModule } from 'ng2-bootstrap';
import { FileUploadModule } from 'ng2-file-upload';

import { DirectiveModule } from '../../../directive/directive.module';
import { SlimLoadingBarModule } from '../../../components/ng2-loading-bar';
import { TreeModule } from '../../../components/ng2-tree';

import { RouteService } from '../../../service/route';
import { RequestService } from '../../../service/request';
import { DatetimePickerService } from '../../../service/datetime-picker';
import { TestcaseService } from '../../../service/testcase';

import { Testcase } from './testcase.component';
import { TestcaseList } from './list/list.component';
import { TestcaseEdit } from './edit/edit.component';

@NgModule({
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgaModule,
    routing,

    TabsModule,
    PaginationModule,
    ModalModule,
    ButtonsModule,
    CollapseModule,
    FileUploadModule,

    DirectiveModule,
    SlimLoadingBarModule.forRoot(),
    TreeModule
  ],
  declarations: [
    Testcase,
    TestcaseList,
    TestcaseEdit
  ],
  providers: [
    RouteService,
    RequestService,
    DatetimePickerService,
    TestcaseService,
  ]
})
export default class TestcaseModule {}
