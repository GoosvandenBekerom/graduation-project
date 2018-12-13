export class FilterStructure {
  public comparisonType: ComparisonType;
  public filters: {
    id: number;
    type: string;
    value: any;
  }[];

  constructor(comparisonType = ComparisonType.AND, filters = []) {
    this.comparisonType = comparisonType;
    this.filters = filters;
  }

  /**
   * Remove any filters without a value or a NULL type from the JSON.stringify result
   */
  toJSON() {
    return Object.assign({}, {
      comparisonType: this.comparisonType,
      filters: this.filters.filter(f => f.type.includes('NULL') || (f.value !== '' && f.value !== undefined && f.value !== null))
    });
  }
}

export enum ComparisonType {
  AND = <any>'AND',
  OR = <any>'OR'
}
