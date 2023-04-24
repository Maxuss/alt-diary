import { getLessons } from "@/api/lessons";
import Layout from "@/components/Layout";
import { LessonCard } from "@/components/LessonCard";
import { RunIcon } from "@/icons/RunIcon";
import { Lesson } from "@/types/lessons";
import { Button, Grid, Spacer, Text } from "@nextui-org/react";
import React, { useEffect, useState } from "react";

type LoadingState = "loading" | "loaded" | "error";

export default function SchedulePage() {
    const [loadingState, setLoadingState] = useState<LoadingState>("loading");
    const [lessons, setLessons] = useState<Lesson[]>([]);
    useEffect(() => {
        const fetchData = async () => {
            const got = await getLessons(new Date(Date.parse("2021-04-20")))
            setLessons(got);
            setLoadingState("loaded");
        }
        fetchData().catch(e => {
            setLoadingState("error")
        })
    }, [])
    return (
        <Layout>
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
                                    lesson.nextBreakDuration == 0 ? <></> : (
                                        <Grid xs={12} key={index}>
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
                    ) : "Error!"
                }
            </Grid.Container>
        </Layout>
    )
}