import { Mark as MarkData } from "@/types/lessons"
import { Badge, Spacer, Text, useTheme } from "@nextui-org/react"

const markBgLight = "bg-gradient-to-bl from-violet-50 to-indigo-100"
const markBgDark = "bg-gradient-to-br from-slate-700 to-gray-800"

export const Mark: React.FC<MarkData> = (mark) => {
    const { isDark } = useTheme()

    return <>
        <div className={`mark-container ${isDark ? markBgDark : markBgLight}`}>
            <Text css={{
                display: "flex",
                position: "relative",
                alignSelf: "center",
                left: "14px",
                fontWeight: 700
            }}>
                {mark.value}
            </Text>
            <Text css={{
                top: "9px",
                left: "19px",
                display: "flex",
                position: "relative",
                fontSize: "10px",
                alignSelf: "center",
                fontWeight: 500,
                lineHeight: 10
            }}>
                {mark.index}
            </Text>
        </div>
    </>
}