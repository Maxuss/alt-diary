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