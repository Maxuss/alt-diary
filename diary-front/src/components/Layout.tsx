import { useTheme } from '@nextui-org/react'
import Bar from './Bar'

export default function Layout({ children }: any) {
    const { theme } = useTheme()
    return (
        <main className={`min-h-screen flex-col items-center justify-between max-w-[80%] mx-auto`} style={{
            backgroundColor: theme?.colors.background.value,
        }}>
            <Bar />
            {children}
        </main >
    )
}