import { NextApiRequest, NextApiResponse } from "next";
import { BASE_API_URL, setupRoute } from "./core";
import axios from "axios";
import { AccountInformation } from "@/api/auth";

export default async function handler(
    req: NextApiRequest,
    res: NextApiResponse<AccountInformation>
) {
    await setupRoute(req, res);

    const accessCookie = req.cookies['accessToken']
    await axios.get(`${BASE_API_URL}/account/self`, {
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