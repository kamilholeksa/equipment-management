import { MatPaginatorIntl } from '@angular/material/paginator';
import { Injectable } from '@angular/core';

@Injectable()
export class PolishPaginatorIntl extends MatPaginatorIntl {
  constructor() {
    super();
    this.itemsPerPageLabel = 'Elementów na stronę';

    const superGetRangeLabel = this.getRangeLabel;
    this.getRangeLabel = (page: number, pageSize: number, length: number) => {
      const originalLabel = superGetRangeLabel(page, pageSize, length);
      return originalLabel.replace('of', 'z');
    };

    this.changes.next();
  }
}
