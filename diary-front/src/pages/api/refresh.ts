import { NextApiRequest, NextApiResponse } from "next";
import NextCors from "nextjs-cors";
import { BASE_API_URL } from "./core";
import axios, { AxiosResponse } from "axios";
import { RegisterData } from "@/api/auth";

export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse<string>
) {
    return;
    // TODO: does not work
    await NextCors(req, res, {
        // Options
        methods: ['GET', 'HEAD', 'PUT', 'PATCH', 'POST', 'DELETE'],
        origin: '*',
        optionsSuccessStatus: 200, // some legacy browsers (IE11, various SmartTVs) choke on 204
        exposedHeaders: ['Set-Cookie']
    });
    console.log("REFRESH");
    const refreshToken = req.cookies['refreshToken']
    console.log(refreshToken);
    const response = await axios.post(`${BASE_API_URL}/student/refresh`, {
        withCredentials: true,
        Headers: {
            'Cookie': `refreshToken=${refreshToken}`
        }
    }).catch(err => {
        console.error(err.response.data)
    }) as AxiosResponse<any, any>
    const cookieHeader = response.headers['set-cookie']
    if (typeof cookieHeader === 'undefined') {
        res.status(response.status).json(response.data)
    } else {
        res.setHeader('Set-Cookie', [...cookieHeader!])
        res.status(response.status).json(response.data)
    }
}