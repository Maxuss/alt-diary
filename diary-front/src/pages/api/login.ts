import { NextApiRequest, NextApiResponse } from "next";
import NextCors from "nextjs-cors";
import { BASE_API_URL, setupRoute } from "./core";
import axios, { AxiosResponse } from "axios";
import { LoginData } from "@/api/auth";


export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse<string>
) {
    await setupRoute(req, res);

    const login = req.body as LoginData
    await axios.post(`${BASE_API_URL}/student/login`, login, {
        headers: { 'Content-Type': 'application/json' }
    })
        .then(resp => {
            const cookieHeader = resp.headers['set-cookie']
            if (typeof cookieHeader === 'undefined') {
                res.status(resp.status).json(resp.data)
            } else {
                res.setHeader('Set-Cookie', [...cookieHeader!])
                res.status(resp.status).json(resp.data)
            }
        }).catch(err => {
            res.status(err.response.status).json(err.response.data)
        })
}