import Layout from "@/components/Layout";
import { LessonCard, LessonCardProps } from "@/components/LessonCard";
import { Grid, Spacer } from "@nextui-org/react";
import React from "react";

const lessonBegin: number = Date.UTC(2022, 3, 16, 7, 30);

const exampleLessons: LessonCardProps[] = [
    {
        lesson: "Алгебра",
        homework: undefined,
        marks: [
            {
                value: 5,
                index: 3
            },
            {
                value: 4,
                index: 2
            }
        ],
        beginTime: lessonBegin,
        endTime: lessonBegin + 45 * 60 * 1000,
    },
    {
        minutes: 15
    },
    {
        lesson: "Физика",
        homework: "Выполнить задачи из файла",
        marks: [
            {
                value: 5,
                index: 1
            }
        ],
        beginTime: lessonBegin + 60 * 60 * 1000,
        endTime: lessonBegin + 105 * 60 * 1000,
    },
    {
        minutes: 15
    },
    {
        lesson: "Химия",
        homework: "Не задано",
        marks: [],
        beginTime: lessonBegin + 120 * 60 * 1000,
        endTime: lessonBegin + 165 * 60 * 1000,
    }
]

export default function SchedulePage() {
    return (
        <Layout>
            <Grid.Container gap={2} justify="center">
                {exampleLessons.map((lesson, index) => (
                    <Grid xs={12} key={index}>
                        <LessonCard key={index} {...lesson} />
                        <Spacer />
                    </Grid>
                ))}
            </Grid.Container>
        </Layout>
    )
}