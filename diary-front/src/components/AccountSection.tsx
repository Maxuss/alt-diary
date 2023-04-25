import { AccountInformation, AccountType, accountInfo } from "@/api/auth"
import { Avatar, Button, Dropdown, Link, Navbar, Text } from "@nextui-org/react"
import { useEffect, useState } from "react"

interface EmbedInformation {
    accountColor: Color
}

type Color = "primary" | "secondary" | "success" | "warning" | "error"

type EmbedAccountInformation = AccountInformation & EmbedInformation;

const accountColors = [
    "primary",
    "secondary",
    "success",
    "warning",
    "error"
]

function chooseAccountColor(name: string, type: AccountType): string {
    const hash = name.length * 31 + type.toString().length * 31
    const color = accountColors[hash % accountColors.length]
    return color
}

export const AccountSection: React.FC<any> = (props) => {
    const [info, setAccountInfo] = useState<EmbedAccountInformation | null>(null)

    useEffect(() => {
        accountInfo().then(res => {
            if ("error" in res) {
                setAccountInfo(null)
            } else {
                setAccountInfo({
                    accountColor: chooseAccountColor(res.name + " " + res.surname, res.accountType) as Color,
                    ...res
                })
            }
        }).catch(err => {
            setAccountInfo(null)
        })
    }, [])

    return (
        <>
            {
                info === null ? (
                    <>
                        <Navbar.Link color="inherit" href="/student/login">
                            Вход
                        </Navbar.Link>
                        <Navbar.Item>
                            <Button auto flat as={Link} href="/student/register">
                                Регистрация
                            </Button>
                        </Navbar.Item>
                    </>
                ) : (
                    <Dropdown placement="bottom-right">
                        <Navbar.Item>
                            <Dropdown.Trigger>
                                <Avatar
                                    color={info!.accountColor}
                                    textColor="white"
                                    text={info!.name[0] + info!.surname[0]}
                                    size="md"
                                />
                            </Dropdown.Trigger>
                        </Navbar.Item>
                        <Dropdown.Menu
                            aria-label="User menu actions"
                            color={info!.accountColor}
                            onAction={(key) => { console.log(key) }}
                        >
                            <Dropdown.Item key="profile" css={{ height: "$18" }}>
                                <Text color="inherit" css={{ d: "flex" }}>
                                    Ученик:
                                </Text>
                                <Text b color="inherit" css={{ d: "flex" }}>
                                    {info!.name} {info!.surname}
                                </Text>
                            </Dropdown.Item>
                            <Dropdown.Item key="settings" withDivider>
                                Настройки
                            </Dropdown.Item>
                            <Dropdown.Item key="help_and_feedback">
                                Помощь
                            </Dropdown.Item>
                            <Dropdown.Item key="logout" withDivider color="error">
                                Выйти
                            </Dropdown.Item>
                        </Dropdown.Menu>
                    </Dropdown>
                )
            }
        </>
    )
}