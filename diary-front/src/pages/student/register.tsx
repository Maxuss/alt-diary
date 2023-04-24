import { register } from "@/api/auth";
import Layout from "@/components/Layout";
import { LoginResult } from "@/components/LoginBox";
import { RegisterBox } from "@/components/RegisterBox";

export default function RegisterPage() {
    const registerSender: (name: string, surname: string, email: string, password: string) => Promise<LoginResult> = async (name: string, surname: string, email: string, password: string) => {
        const result = await register({ name, surname, email, password }).catch(err => {
            console.error(err);
        })
        return result === true ? "success" : { error: result as string };
    }

    return (
        <Layout>
            <RegisterBox onSend={registerSender} />
        </Layout>
    )

}