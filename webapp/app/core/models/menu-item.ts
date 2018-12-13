import {Resource} from './resource';
import {Action} from './action';

export class MenuItem extends Resource {
  text: string;
  depth: number;
  icon: string;
  parent: MenuItem;
  children: MenuItem[];
  action: Action;
  leaf: boolean;
}
