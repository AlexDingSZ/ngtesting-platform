import {Component, ViewEncapsulation, ViewChild} from '@angular/core';
import { Router, ActivatedRoute, Params } from '@angular/router';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { NgModule, Pipe, OnInit, AfterViewInit }      from '@angular/core';
import { ModalDirective } from 'ng2-bootstrap';
import {GlobalState} from '../../../../../global.state';

import { CONSTANT } from '../../../../../utils/constant';
import { Utils } from '../../../../../utils/utils';
import {ValidatorUtils, CustomValidator} from '../../../../../validator';
import { RouteService } from '../../../../../service/route';

import { CustomFieldService } from '../../../../../service/custom-field';
import { PopDialogComponent } from '../../../../../components/pop-dialog'

declare var jQuery;

@Component({
  selector: 'custom-field-edit',
  encapsulation: ViewEncapsulation.None,
  styles: [require('./edit.scss')],
  template: require('./edit.html')
})
export class CustomFieldEdit implements OnInit, AfterViewInit {

  id: number;
  tab: string = 'info';

  model: any = {isGlobal: true, rows: 3};
  applyToList: string[];
  typeList: string[];
  formatList: string[];

  relations: any[] = [];
  form: FormGroup;
  isSubmitted: boolean;
  @ViewChild('modalWrapper') modalWrapper: PopDialogComponent;

  constructor(private _state:GlobalState, private _routeService: RouteService, private _route: ActivatedRoute,
              private fb: FormBuilder, private customFieldService: CustomFieldService) {

  }
  ngOnInit() {
    this._route.params.forEach((params: Params) => {
      this.id = +params['id'];
    });

    this.loadData();
    this.buildForm();
  }
  ngAfterViewInit() {}


  selectTab(tab: string) {
    let that = this;
    that.tab = tab;
  }

  buildForm(): void {
    let that = this;
    this.form = this.fb.group(
      {
        'name': ['', [Validators.required]],
        'code': ['', []],
        'applyTo': ['', [Validators.required]],
        type: ['', [Validators.required]],
        rows:  ['', [Validators.pattern('^[1-9]$'), CustomValidator.validate('required_if_other_is', 'required_rows', 'rows', 'type', 'text')]],
        format: ['', [CustomValidator.validate('required_if_other_is', 'required_format', 'format', 'type', 'text')]],
        descr: ['', []],
        isGlobal: ['', []],
        isRequired: ['', []]
      }, {}
    );

    this.form.valueChanges.subscribe(data => this.onValueChanged(data));
    this.onValueChanged();
  }
  onValueChanged(data?: any) {
    let that = this;
    that.formErrors = ValidatorUtils.genMsg(that.form, that.validateMsg, []);
  }

  formErrors = [];
  validateMsg = {
    'name': {
      'required':      '姓名不能为空'
    },
    'applyTo': {
      'required':      '应用对象不能为空'
    },
    'type': {
      'required':      '类型不能为空'
    },
    'rows': {
      'pattern': '字段行数必须为1-9的整数',
      'required_rows':      '字段行数不能为空'
    },
    'format': {
      'required_format':      '字段格式不能为空'
    }
  };

  loadData() {
    let that = this;
    that.customFieldService.get(that.id).subscribe((json:any) => {
      that.model = json.data;

      that.applyToList = json.applyToList;
      that.typeList = json.typeList;
      that.formatList = json.formatList;
      that.relations = json.projects;

      _.forEach(that.relations, (project: any, index: number) => {
        this.form.addControl('project-' + project.id, new FormControl('', []))
      });
    });
  }

  save() {
    let that = this;

    that.customFieldService.save(that.model, that.relations).subscribe((json:any) => {
      if (json.code == 1) {

        that.formErrors = ['保存成功'];
        that._routeService.navTo("/pages/org-admin/property/custom-field/list");
      } else {
        that.formErrors = [json.msg];
      }
    });
  }

  delete() {
    let that = this;

    that.customFieldService.delete(that.model.id).subscribe((json:any) => {
      if (json.code == 1) {
        that.formErrors = ['删除成功'];
        that._routeService.navTo("/pages/org-admin/field/list");
      } else {
        that.formErrors = ['删除失败'];
      }
    });
  }

  select(key: string) {
    let val = key ==='all'? true: false;
    for (let group of this.relations) {
      group.selecting = val;
    }
  }

  showModal(): void {
    this.modalWrapper.showModal();
  }

}
