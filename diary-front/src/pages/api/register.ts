import { NextApiRequest, NextApiResponse } from "next";
import { BASE_API_URL, setupRoute } from "./core";
import axios from "axios";
import { RegisterData } from "@/api/auth";

export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse<string>
) {
    await setupRoute(req, res);

    const login = req.body as RegisterData
    console.log(login)
    axios.post(`${BASE_API_URL}/student/register`, login, {
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            const cookieHeader = response.headers['set-cookie']
            if (typeof cookieHeader === 'undefined') {
                res.status(response.status).json(response.data)
            } else {
                res.setHeader('Set-Cookie', [...cookieHeader!])
                res.status(response.status).json(response.data)
            }
        })
        .catch(err => {
            console.error(err.response.data)
        })
}