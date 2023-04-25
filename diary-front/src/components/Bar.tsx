import { Navbar, Spacer, Text, Link, Button } from "@nextui-org/react"
import ThemeSwitch from "./ThemeSwitch"
import { useRouter } from "next/router"
import React from "react"
import { AcmeLogo } from "@/icons/AcmeLogo"
import { AccountSection } from "./AccountSection"

const items: { display: string, link: string }[] = [
    {
        display: "Расписание",
        link: "/schedule"
    },
    {
        display: "Оценки",
        link: "/marks"
    },
    {
        display: "Задания",
        link: "/homework"
    },
]

export default function Bar() {
    const router = useRouter()

    return <Navbar isBordered variant="sticky" disableShadow>
        <Navbar.Brand>
            <Navbar.Toggle aria-label="toggle navigation" showIn="xs" />
            <AcmeLogo />
            <Spacer />
            <Text b color="inherit" hideIn="sm">
                Дневник
            </Text>
        </Navbar.Brand>
        <Navbar.Content hideIn="xs" enableCursorHighlight variant="underline-rounded">
            {items.map((item, idx) => (
                <React.Fragment key={idx}>
                    <Navbar.Link isActive={router.pathname === item.link} href={item.link}>{item.display}</Navbar.Link>
                    <Spacer key={idx + 10} />
                </React.Fragment>
            ))}
        </Navbar.Content>
        <Navbar.Content>
            <AccountSection />
            <Navbar.Item>
                <ThemeSwitch />
            </Navbar.Item>
        </Navbar.Content>
        <Navbar.Collapse transitionTime={450} key={3}>
            {items.map((item, idx) => (
                <Navbar.CollapseItem key={idx} isActive={router.pathname === item.link}>
                    <Link
                        color="inherit"
                        css={{
                            minWidth: "100%",
                        }}
                        href={item.link}
                    >
                        {item.display}
                    </Link>
                </Navbar.CollapseItem>
            ))}
        </ Navbar.Collapse>
    </Navbar>

}