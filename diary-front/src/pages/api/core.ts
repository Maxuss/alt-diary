import type { NextApiRequest, NextApiResponse } from "next";
import NextCors from "nextjs-cors";

export const BASE_API_URL = 'https://api.maxus.space';

export async function setupRoute(
    req: NextApiRequest,
    res: NextApiResponse
) {
    await NextCors(req, res, {
        // Options
        methods: ['GET', 'HEAD', 'PUT', 'PATCH', 'POST', 'DELETE'],
        origin: '*',
        optionsSuccessStatus: 200, // some legacy browsers (IE11, various SmartTVs) choke on 204
        credentials: true,
    });
}