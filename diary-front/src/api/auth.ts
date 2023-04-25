import { get, post } from "./core"

export interface LoginData {
    email: string,
    password: string
}

export interface RegisterData {
    name: string,
    surname: string,
    email: string,
    password: string
}

export interface AccountInformation {
    name: string,
    surname: string,
    patronymic: string | null,
    accountType: AccountType,
    username: string | null,
    verified: boolean,
}

export enum AccountType {
    ADMIN,
    TEACHER,
    STUDENT
}

export async function register(data: RegisterData): Promise<true | string> {
    const response = (await post("/register", data)).data
    return response.success ? true : response.message;
}

export async function login(data: LoginData): Promise<true | string> {
    const response = (await post("/login", data)).data
    return response.success ? true : response.message;
}

export async function refresh(): Promise<true | string> {
    const response = (await get("/refresh")).data
    return response.success ? true : response.message;
}

export async function accountInfo(): Promise<AccountInformation | { error: string }> {
    const response = (await get("/account-info")).data
    return response.success ? response as AccountInformation : { error: response.message }
}