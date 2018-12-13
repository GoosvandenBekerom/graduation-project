import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class TimingService {

  times = new Map<string, number>();

  start(label: string) {
    this.times.set(label, Date.now());
  }

  end(label: string): string {
    const startTime = this.times.get(label);
    this.times.delete(label);
    const endTime = Date.now();
    return `${(endTime - startTime)}ms`;
  }
}
