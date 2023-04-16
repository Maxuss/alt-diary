import { Card, Grid, Spacer, Text, useTheme } from "@nextui-org/react";
import { MarkData, Mark } from "./Mark";
import { HomeIcon } from "@/icons/HomeIcon";
import { RunIcon } from "@/icons/RunIcon";

export interface Lesson {
    lesson: string,
    homework: string | undefined,
    marks: MarkData[],
    beginTime: number,
    endTime: number
}

export interface Break {
    minutes: number
}

export type LessonCardProps = Lesson | Break

export const LessonCard: React.FC<LessonCardProps> = (props) => {
    const { theme } = useTheme()

    if ("minutes" in props) {
        return (
            <div className="flex flex-auto">
                <RunIcon fill="var(--nextui-colors-textLight)" transform="scale(0.7)" />
                <Spacer inline x={0.15} y={0} />
                <Text color="$textLight">Перемена {props.minutes} мин.</Text>
            </div>
        )
    }

    const { lesson, homework, marks, beginTime, endTime } = props
    const beginDate = new Date(beginTime)
    const endDate = new Date(endTime)
    const beginFmt = `${beginDate.getHours().toString().padStart(2, "0")}:${beginDate.getMinutes().toString().padStart(2, "0")}`;
    const endFmt = `${endDate.getHours().toString().padStart(2, "0")}:${endDate.getMinutes().toString().padStart(2, "0")}`;

    return (
        <Card variant="bordered" css={{ py: "$10" }}>
            <Card.Header>
                <Text b>
                    {lesson}
                </Text>

                <Spacer y={0} x={1.5} />

                <Text color="$textLight">
                    {beginFmt} - {endFmt}
                </Text>
            </Card.Header>
            <Card.Divider />
            <Card.Body css={{ display: "flex", flexDirection: "column", overflow: "hidden" }}>
                {homework === undefined ? (
                    <div className="flex flex-auto">
                        <HomeIcon fill={theme?.colors.text.value} transform="scale(0.7)" />
                        <Spacer inline x={0.15} y={0} />
                        <Text b>
                            Домашнего задания нет
                        </Text>
                    </div>

                ) : (
                    <>
                        <div className="flex">
                            <HomeIcon filled fill={theme?.colors.text.value} transform="scale(0.7)" />
                            <Spacer inline x={0.15} y={0} />
                            <Text b>
                                Домашнее задание
                            </Text>
                        </div>

                        <div className="flex">
                            <Spacer x={0.2} y={0} />
                            <div>
                                <Text color="$textLight">
                                    {homework}
                                </Text>
                            </div>
                        </div>
                    </>
                )}
                <Grid.Container gap={1}>
                    {marks.map((mark, index) => (
                        <Grid key={index}>
                            <Mark key={index} {...mark} />
                        </Grid>
                    ))}
                </Grid.Container>
            </Card.Body>
        </Card >
    )
}