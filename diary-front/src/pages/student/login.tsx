import { login } from "@/api/auth";
import Layout from "@/components/Layout";
import { LoginBox, LoginResult } from "@/components/LoginBox";

export default function LoginPage() {
    const loginSender: (email: string, password: string) => Promise<LoginResult> = async (email: string, password: string) => {
        const result = await login({ email, password }).catch(err => {
            return err.response.data.message
        });
        return result === true ? "success" : { error: result as string };
    }

    return (
        <Layout>
            <LoginBox onSend={loginSender} />
        </Layout>
    )
}
