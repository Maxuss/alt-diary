import { Roboto } from 'next/font/google'
import Layout from '@/components/Layout'
import { Text } from '@nextui-org/react';
import { useEffect } from 'react';
import { useRouter } from 'next/router';
import { post } from '@/api/core';
import { refresh } from '@/api/auth';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    // TODO: refreshing
    if (getCookie("refreshToken") === undefined) {
      router.push("/student/login");
      return;
    } else {
      router.push("/schedule");
    }
  }, [router]);

  return (
    <Layout>
      <Text>
        Redirecting you to schedule...
      </Text>
    </Layout>
  );
}

function getCookie(key: string): string | undefined {
  var b = document.cookie.match("(^|;)\\s*" + key + "\\s*=\\s*([^;]+)");
  return b ? b.pop() : undefined;
}