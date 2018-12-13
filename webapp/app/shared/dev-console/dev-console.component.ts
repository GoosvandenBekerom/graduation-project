import { Component, OnInit } from '@angular/core';
import {Observable} from 'rxjs';
import { DevService } from '../../core/services/util/dev.service';

@Component({
  selector: 'app-dev-console',
  templateUrl: './dev-console.component.html',
  styleUrls: ['./dev-console.component.css']
})
export class DevConsoleComponent implements OnInit {

  devConsole$: Observable<string>;

  constructor(private devService: DevService) { }

  ngOnInit() {
    this.devConsole$ = this.devService.output;
  }
}
