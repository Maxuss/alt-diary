import { refresh } from "@/api/auth";
import { getLessons } from "@/api/lessons";
import Layout from "@/components/Layout";
import { LessonCard } from "@/components/LessonCard";
import { LessonPager, buildWeek, mapDate } from "@/components/LessonPager";
import { RunIcon } from "@/icons/RunIcon";
import { Lesson } from "@/types/lessons";
import { Button, Grid, Spacer, Text } from "@nextui-org/react";
import React, { useEffect, useState } from "react";

type LoadingState = "loading" | "loaded" | { error: string };

export default function SchedulePage() {
    const [loadingState, setLoadingState] = useState<LoadingState>("loading");
    const [lessons, setLessons] = useState<Lesson[]>([]);
    const [cachedDays, setCachedDays] = useState<Map<number, Lesson[]>>(new Map());
    const week = buildWeek(new Date(Date.parse("2023-04-24")));
    const weekStr = week.map(mapDate)

    const fetchData = async (date: Date) => {
        if (cachedDays.has(date.getTime())) {
            console.log("GOT")
            setLessons(cachedDays.get(date.getTime())!)
            return;
        }
        setLoadingState("loading");
        const got = await getLessons(date);
        const cachedDaysCopy = cachedDays
        cachedDaysCopy.set(date.getTime(), got);
        setCachedDays(cachedDaysCopy);
        console.log("SET")
        setLessons(got);
        setLoadingState("loaded");
    }
    useEffect(() => {
        refresh().then(_ => {
            // everything is fine
            fetchData(week[0]).catch(e => {
                setLoadingState({ error: e.error })
            })
        }).catch(err => console.error(err))
    }, [])

    const dayChanger = (newDay: Date) => {
        fetchData(newDay).catch(e => {
            setLoadingState({ error: e.error })
        })
    }
    return (
        <Layout>
            <LessonPager dates={week} datesStr={weekStr} onDateChange={dayChanger} />
            <Grid.Container gap={2} justify="center">
                {
                    loadingState == "loading" ? "Loading..." : loadingState == "loaded" ? (
                        lessons.map((lesson, index) => (
                            <>
                                <Grid xs={12} key={index}>
                                    <LessonCard key={index} {...lesson} />
                                    <Spacer />
                                </Grid>
                                {
                                    index === lessons.length - 1 ? <React.Fragment key={100 + index}></React.Fragment> : (
                                        <Grid xs={12} key={100 + index}>
                                            <div className="flex flex-auto">
                                                <RunIcon fill="var(--nextui-colors-textLight)" transform="scale(0.7)" />
                                                <Spacer inline x={0.15} y={0} />
                                                <Text color="$textLight">Перемена {lesson.nextBreakDuration} мин.</Text>
                                            </div>
                                            <Spacer />
                                        </Grid>
                                    )
                                }
                            </>
                        ))
                    ) : loadingState.error
                }
            </Grid.Container>
        </Layout>
    )
}