import { Lesson } from "@/types/lessons";
import { post } from "./core";

export async function getLessons(date: Date): Promise<Lesson[]> {
    return (await post(`/lessons`, { date: date })).data.lessons;
}