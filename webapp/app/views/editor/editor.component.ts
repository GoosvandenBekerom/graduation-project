import {Component, Host, OnInit} from '@angular/core';
import {ContentComponent} from 'content/content.component';

@Component({
  selector: 'app-editor',
  templateUrl: './editor.component.html',
  styleUrls: ['./editor.component.css']
})
export class EditorComponent implements OnInit {

  menuTabActive: boolean;
  actionTabActive: boolean;
  overviewTabActive: boolean;

  constructor(@Host() private content: ContentComponent) { }

  ngOnInit() {
    this.content.collapseNavigation();
  }
}
