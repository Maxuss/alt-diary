import { Card, Container, Input, Row, Spacer, Text, Button, Loading } from "@nextui-org/react"
import { MutableRefObject, useRef, useState } from "react"
import { ButtonState, StatusState, ValueErrorState } from "./RegisterBox"
import { useRouter } from "next/router"

export interface LoginBoxProps {
    onSend: (email: string, password: string) => Promise<LoginResult>
}

export type LoginResult = "success" | { error: string }

export const LoginBox: React.FC<LoginBoxProps> = (props) => {
    const router = useRouter();

    const emailInputRef: MutableRefObject<any> = useRef(null);
    const passwordInputRef: MutableRefObject<any> = useRef(null);

    const [emailState, setEmailState] = useState<StatusState>("none")
    const [errorState, setErrorState] = useState<ValueErrorState>("fine")
    const [emptyState, setEmptyState] = useState<StatusState>("none")
    const [buttonState, setButtonState] = useState<ButtonState>("none")

    return (
        <Container display="flex" alignItems="center" justify="center" className="pt-[10%] lg:max-w-[50%] min-h-[50%] sm:max-w-[100%]">
            <Card className="mw-[400px] p-[20px]" variant="bordered">
                <Text
                    size={30}
                    b
                    className="text-center mb-[10px]"
                >
                    Вход в аккаунт ученика
                </Text>
                <Card.Divider />
                <Spacer />
                <Input
                    clearable
                    bordered
                    fullWidth
                    color="primary"
                    size="lg"
                    placeholder="Почта"
                    status={emailState == "error" ? "error" : "default"}
                    helperText={emailState == "error" ? "Неверный адрес почты" : ""}
                    onInput={() => { setEmailState("none"); setEmptyState("none") }}
                    ref={emailInputRef}
                />
                <Spacer y={1} />
                <Input.Password
                    clearable
                    bordered
                    fullWidth
                    color="primary"
                    size="lg"
                    placeholder="Пароль"
                    ref={passwordInputRef}
                    onInput={() => setEmptyState("none")}
                />
                <Spacer y={1} />
                <Text
                    color="error"
                    size={14}
                    b
                    className="text-center mb-[10px]"
                    hidden={emptyState === "none" && errorState === "fine"}
                >
                    {
                        errorState === "fine" ? "Заполните все поля для продолжения!" : errorState.error
                    }
                </Text>
                <Button
                    type="submit"
                    auto
                    css={{ background: "$primaryGradient" }}
                    disabled={buttonState === "loading"}
                    bordered={buttonState === "loading"}
                    onClick={() => {
                        const email: string = emailInputRef.current.value;
                        const password: string = passwordInputRef.current.value;

                        if (email.length === 0 || password.length === 0) {
                            setEmptyState("error")
                            return;
                        }

                        if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(email)) {
                            setEmailState("error")
                            return;
                        }

                        props.onSend(emailInputRef.current.value, passwordInputRef.current.value).then(value => {
                            if (value === "success") {
                                // everything fine
                                router.replace("/student/login", "/schedule")
                                router.reload();
                            } else {
                                // an error occurred
                                setErrorState(value)
                                setButtonState("none")
                            }
                        }
                        );
                        setButtonState("loading")
                    }}>
                    <div hidden={buttonState === "none"}>
                        <Loading
                            color="currentColor"
                            size="md"
                            type="points"
                        />
                    </div>
                    <Text b size={16} color="white" hidden={buttonState === "loading"}>
                        Войти
                    </Text>
                </Button>
            </Card>
        </Container>
    )
}