export interface Page<T> {
  content: T[];
  page: {
    totalElements: number;
  };
}
