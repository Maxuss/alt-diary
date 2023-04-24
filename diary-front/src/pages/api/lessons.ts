import { Lesson } from "@/types/lessons";
import { NextApiRequest, NextApiResponse } from "next";
import NextCors from "nextjs-cors";
import { BASE_API_URL } from "./core";
import axios from "axios";

export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse<Lesson[]>
) {
    await NextCors(req, res, {
        // Options
        methods: ['GET', 'HEAD', 'PUT', 'PATCH', 'POST', 'DELETE'],
        origin: '*',
        optionsSuccessStatus: 200, // some legacy browsers (IE11, various SmartTVs) choke on 204
        credentials: true,
    });

    const date: Date = new Date(Date.parse(req.body.body.date))
    const cookies = Object.keys({ ...req.cookies }).map(key => `${key}=${req.cookies[key]}`).join("; ")
    const url = `${BASE_API_URL}/lessons?date=${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
    const response = await axios.get(url, {
        withCredentials: true,
        headers: {
            Cookie: cookies
        }
    })
    res.status(response.status).json(response.data)
}