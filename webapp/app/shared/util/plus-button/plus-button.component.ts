import {Component, Input} from '@angular/core';

@Component({
  selector: 'app-plus-button',
  templateUrl: './plus-button.component.html',
  styleUrls: ['./plus-button.component.css']
})
export class PlusButtonComponent {
  @Input() size = 22;
  hovered = false;
}
