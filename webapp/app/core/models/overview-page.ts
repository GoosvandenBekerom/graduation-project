export interface IOverviewPage {
  content: any[];
  page: number;
  size: number;
  totalCount: number;
}

export class OverviewPage implements IOverviewPage {
  constructor(
    public content: any[],
    public page: number,
    public size: number,
    public totalCount: number
  ) {}
}
