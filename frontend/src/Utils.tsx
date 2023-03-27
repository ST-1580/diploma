export const BACKEND_URL = 'http://localhost:8080/';

export function isCustomNumeric(data: string): boolean {
    return data === "" || data === "-" || Number.isInteger(data);
}