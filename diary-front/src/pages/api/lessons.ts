import { Lesson } from "@/types/lessons";
import { NextApiRequest, NextApiResponse } from "next";
import { BASE_API_URL, setupRoute } from "./core";
import axios from "axios";

export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse<Lesson[]>
) {
    await setupRoute(req, res);

    const date: Date = new Date(Date.parse(req.body.date));
    const accessCookie = req.cookies['accessToken']
    const url = `${BASE_API_URL}/lessons?date=${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
    await axios.get(url, {
        withCredentials: true,
        headers: {
            'Cookie': `accessToken=${accessCookie}`
        }
    })
        .then(response => {
            res.status(response.status).json(response.data)
        })
        .catch(err => {
            res.status(err.response.status).json(err.response.data)
        })
}