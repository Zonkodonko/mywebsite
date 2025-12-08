export default function isString(value: any): value is string { return typeof value === 'string' || value instanceof String; }

export function isStringEmpty(value: string): boolean {
  return value == null || value.trim().length === 0;
}

export function findAndDelete(array: any[], predicate: (item: any) => boolean) {
  const index = array.findIndex(predicate);
  if (index >= 0) {
    array.splice(index, 1);
  }
}
