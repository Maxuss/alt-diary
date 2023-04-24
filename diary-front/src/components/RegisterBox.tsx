import { Card, Container, Input, Row, Spacer, Text, Button, Grid, Loading } from "@nextui-org/react"
import { MutableRefObject, useRef, useState } from "react"
import { LoginResult } from "./LoginBox";
import { useRouter } from "next/router";

export interface RegisterBoxProps {
    onSend: (name: string, surname: string, email: string, password: string) => Promise<LoginResult>
}

export type StatusState = "none" | "error";
export type ButtonState = "none" | "loading";
export type ValueErrorState = "fine" | { error: string };

export const RegisterBox: React.FC<RegisterBoxProps> = (props) => {
    const router = useRouter();

    const nameInputRef: MutableRefObject<any> = useRef(null);
    const surnameInputRef: MutableRefObject<any> = useRef(null);
    const emailInputRef: MutableRefObject<any> = useRef(null);
    const passwordInputRef: MutableRefObject<any> = useRef(null);
    const passwordInputRefRepeat: MutableRefObject<any> = useRef(null);

    const [passwordState, setPasswordState] = useState<StatusState>("none")
    const [emailState, setEmailState] = useState<StatusState>("none")
    const [errorState, setErrorState] = useState<ValueErrorState>("fine")
    const [emptyState, setEmptyState] = useState<StatusState>("none")
    const [buttonState, setButtonState] = useState<ButtonState>("none")

    return (
        <Container display="flex" alignItems="center" justify="center" className="pt-[10%] lg:max-w-[50%] min-h-[50%] sm:max-w-[100%]">
            <Card className="mw-[420px] p-[20px]" variant="bordered">
                <Text
                    size={30}
                    b
                    className="text-center mb-[10px]"
                >
                    Создать аккаунт ученика
                </Text>
                <Card.Divider />
                <Grid.Container gap={2} justify="center" className="px-0">
                    <Grid xs>
                        <Input
                            clearable
                            bordered
                            fullWidth
                            color="primary"
                            size="lg"
                            placeholder="Имя"
                            ref={nameInputRef}
                        />
                    </Grid>
                    <Grid xs>
                        <Input
                            clearable
                            bordered
                            fullWidth
                            color="primary"
                            size="lg"
                            placeholder="Фамилия"
                            ref={surnameInputRef}
                        />
                    </Grid>
                </Grid.Container>
                <Spacer y={0.5} />
                <Input
                    clearable
                    bordered
                    fullWidth
                    color="primary"
                    size="lg"
                    placeholder="Почта"
                    ref={emailInputRef}
                    status={emailState == "error" ? "error" : "default"}
                    helperText={emailState == "error" ? "Неверный адрес почты" : ""}
                    onInput={() => setEmailState("none")}
                />
                <Spacer y={1} />
                <Input.Password
                    clearable
                    bordered
                    fullWidth
                    color="primary"
                    size="lg"
                    status={passwordState == "error" ? "error" : "default"}
                    helperText={passwordState == "error" ? "Пароли не совпадают" : ""}
                    placeholder="Пароль"
                    ref={passwordInputRef}
                    onInput={() => setPasswordState("none")}
                />
                <Spacer y={1} />
                <Input.Password
                    clearable
                    bordered
                    fullWidth
                    color="primary"
                    size="lg"
                    status={passwordState == "error" ? "error" : "default"}
                    helperText={passwordState == "error" ? "Пароли не совпадают" : ""}
                    placeholder="Пароль повторно"
                    ref={passwordInputRefRepeat}
                    onInput={() => setPasswordState("none")}
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
                    onPress={() => {
                        const passwordFirst: string = passwordInputRef.current.value;
                        const passwordSecond: string = passwordInputRefRepeat.current.value;
                        const name: string = nameInputRef.current.value;
                        const surname: string = surnameInputRef.current.value;
                        const email: string = emailInputRef.current.value;
                        if (
                            name.length === 0 || surname.length === 0 ||
                            email.length === 0 || passwordFirst.length === 0 ||
                            passwordSecond.length === 0
                        ) {
                            setEmptyState("error")
                            return;
                        }
                        if (passwordFirst !== passwordSecond) {
                            setPasswordState("error")
                            return;
                        }
                        if (!/^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,4}$/i.test(email)) {
                            setEmailState("error")
                            return;
                        }
                        setPasswordState("none")
                        props.onSend(nameInputRef.current.value, surnameInputRef.current.value, emailInputRef.current.value, passwordInputRef.current.value).then(value => {
                            if (value === "success") {
                                // everything fine
                                router.replace("/student/register", "/schedule");
                                router.reload();
                            } else {
                                // an error occurred
                                setErrorState(value)
                                setButtonState("none")
                            }
                        });
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
                        Создать аккаунт
                    </Text>
                </Button>
            </Card>
        </Container >
    )
}