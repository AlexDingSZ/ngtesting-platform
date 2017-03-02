import { Input, Component, OnInit, EventEmitter, Output, Inject, OnChanges, SimpleChanges } from '@angular/core';
import { TreeService } from './tree.service';
import { TreeModel, Ng2TreeSettings, Ng2TreeOptions } from './tree.types';
import { NodeEvent } from './tree.events';
import { Tree } from './tree';

import { Utils } from '../../../../utils/utils';

@Component({
  selector: 'tree',
  template: `
    <div>
      <tree-toolbar [tree]="tree" [options]="options"></tree-toolbar>
    </div>
     <div class="y-scrollable" [style.height]="mainHeight">
      <tree-internal [tree]="tree" [options]="options"></tree-internal>
    </div>
   `,
  providers: [TreeService]
})
export class TreeComponent implements OnInit, OnChanges {
  private static EMPTY_TREE: Tree = new Tree({id: undefined, value: '', type: 0});

  /* tslint:disable:no-input-rename */
  @Input('tree')
  public treeModel: TreeModel;
  /* tslint:enable:no-input-rename */

  @Input()
  public settings: Ng2TreeSettings;

    @Input()
    public options: Ng2TreeOptions;

  @Output()
  public nodeCreatedRemote: EventEmitter<any> = new EventEmitter();

    @Output()
    public nodeRemovedRemote: EventEmitter<any> = new EventEmitter();

  @Output()
  public nodeRenamedRemote: EventEmitter<any> = new EventEmitter();

  @Output()
  public nodeMovedRemote: EventEmitter<any> = new EventEmitter();

  @Output()
  public nodeSelected: EventEmitter<any> = new EventEmitter();

  public tree: Tree;
  mainHeight: string;

  public constructor(@Inject(TreeService) private treeService: TreeService) {
    this.mainHeight = Utils.getScreenSize().h - 110 + 'px';
  }

  public ngOnChanges(changes: SimpleChanges): void {
    if (!this.treeModel) {
      this.tree = TreeComponent.EMPTY_TREE;
    } else {
      this.tree = Tree.buildTreeFromModel(this.treeModel);
    }
  }

  public ngOnInit(): void {
      let that = this;

      this.treeService.nodeRemovedRemote$.subscribe((e: NodeEvent) => {
          this.nodeRemovedRemote.emit(e);
      });

    this.treeService.nodeRenamedRemote$.subscribe((e: NodeEvent) => {
      this.nodeRenamedRemote.emit(e);
    });

    this.treeService.nodeCreatedRemote$.subscribe((e: NodeEvent) => {
      this.nodeCreatedRemote.emit(e);
    });

    this.treeService.nodeMovedRemote$.subscribe((e: NodeEvent) => {
      this.nodeMovedRemote.emit(e);
    });

    this.treeService.nodeSelected$.subscribe((e: NodeEvent) => {
      this.nodeSelected.emit(e);
    });

  }
}
