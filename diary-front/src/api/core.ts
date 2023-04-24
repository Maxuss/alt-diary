import axios from "axios"

export const BASE_API_URL: string = "http://dnevnik.maxus.space/api"

export async function post(url: string, body: any = {}): Promise<any> {
    return await axios.post(BASE_API_URL + url, {
        body: body,
        withCredentials: true
    })
}

export async function get(url: string): Promise<any> {
    return await axios.post(BASE_API_URL + url, {
        withCredentials: true
    })
}