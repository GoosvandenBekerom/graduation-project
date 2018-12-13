import { Injectable } from '@angular/core';
import {Subject} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DevService {

  readonly output: Subject<string> = new Subject<string>();

  log(message: string) {
    this.output.next(message);
  }
}
