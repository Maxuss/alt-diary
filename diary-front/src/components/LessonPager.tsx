import { Text, Card, Grid, Pagination, PaginationProps, Spacer, StyledPagination, StyledPaginationItem, Button, useTheme } from "@nextui-org/react"
import { useCallback, useState } from "react"
import { mergeProps } from "react-aria"

export interface LessonPagerProps {
    datesStr: string[]
    dates: Date[]
    onDateChange: (date: Date) => void
}

const genRange = (begin: number, end: number): number[] => {
    const length = end - begin + 1;

    return Array.from({ length }, (_, index) => index + begin);
}

export const LessonPager: React.FC<LessonPagerProps> = ({
    dates,
    datesStr,
    onDateChange,
}) => {
    const { isDark } = useTheme()
    const today = new Date(Date.now()).getDate()
    const initialPage = dates.indexOf(dates.find((v) => v.getDate() === today)!)
    const [active, setActive] = useState(initialPage)
    const setPage = useCallback((number: number) => {
        setActive(number);
        onDateChange(dates[number - 1])
    }, [setActive, onDateChange])
    const range = genRange(1, dates.length)

    return (
        <div className="">
            <Spacer y={1} />
            <Pagination
                className={"lesson-pages mx-[12%] md:mx-[30%] max-w-[100%]"}
                noMargin
                controls={false}
                total={dates.length}
                onChange={(pg) => setPage(pg)}
                size="xl"
            />
            <Button.Group
                size="lg"
                className="lesson-selector max-h-screen flex flex-row items-center justify-center sm:max-w-[100%] md:max-w-[80%] lg:max-w-[70%] mx-auto"
            >
                {
                    range.map((v, idx) =>
                        <Button
                            disabled={active === idx + 1}
                            color="primary"
                            onPress={() => setPage(idx + 1)}
                            key={idx}
                            css={{
                                background: "$gray100"
                            }}
                            shadow
                            className="button-group"
                        >
                            <Text b color={isDark ? "white" : active === idx + 1 ? "white" : "black"}>
                                {datesStr[idx]}
                            </Text>
                        </Button>
                    )
                }
            </Button.Group>

            <Spacer y={1} />
        </div >
    )
}

export function mapDate(date: Date): string {
    return date.toLocaleDateString("ru-RU", {
        year: "numeric",
        month: "long",
        day: "numeric",
    })
}

export function buildWeek(monday: Date): Date[] {
    const week: Date[] = []
    for (let i = 0; i < 7; i++) {
        week.push(new Date(monday.getTime() + i * 24 * 60 * 60 * 1000))
    }
    return week
}