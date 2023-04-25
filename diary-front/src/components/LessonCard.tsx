import { Card, Grid, Spacer, Text, useTheme } from "@nextui-org/react";
import { Mark } from "./Mark";
import { HomeIcon } from "@/icons/HomeIcon";
import { RunIcon } from "@/icons/RunIcon";
import { Lesson } from "@/types/lessons";

export const LessonCard: React.FC<Lesson> = ({
    id, beginTime, endTime, subject, marks, homework
}) => {
    const { theme } = useTheme()

    const beginDateTimeArr = beginTime.split(":")
    beginDateTimeArr.pop()
    const beginFmt = beginDateTimeArr.join(":")
    const endDateTimeArr = endTime.split(":")
    endDateTimeArr.pop()
    const endFmt = endDateTimeArr.join(":")

    return (
        <>
            <Card variant="bordered" css={{ py: "$xs" }}>
                <Card.Header>
                    <Text b>
                        {subject.name}
                    </Text>

                    <Spacer y={0} x={1.5} />

                    <Text color="$textLight">
                        {beginFmt} - {endFmt}
                    </Text>
                </Card.Header>
                <Card.Divider />
                <Card.Body css={{ display: "flex", flexDirection: "column", overflow: "hidden" }}>
                    {homework.none ? (
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
                                        {homework.summary}
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
        </>
    )
}