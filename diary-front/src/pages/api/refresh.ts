import { NextApiRequest, NextApiResponse } from "next";
import { BASE_API_URL, setupRoute } from "./core";
import axios from "axios";

export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse<string>
) {
    await setupRoute(req, res)
    const refresh = req.cookies['refreshToken']
    await axios.post(`${BASE_API_URL}/student/refresh`, undefined, {
        // withCredentials: true,
        headers: {
            'Cookie': `refreshToken=${refresh}`,
        }
    })
        .then(resp => {
            const cookieHeader = resp.headers['set-cookie']
            if (typeof cookieHeader === 'undefined') {
                res.status(resp.status).json(resp.data)
            } else {
                res.setHeader('Set-Cookie', [...cookieHeader!])
                res.status(resp.status).json(resp.data)
            }
        })
        .catch(err => {
            res.status(err.response.status).json(err.response.data)
        })
}