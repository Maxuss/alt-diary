import { Roboto } from 'next/font/google'
import Layout from '@/components/Layout'
import { Text } from '@nextui-org/react';
import { useEffect } from 'react';
import { useRouter } from 'next/router';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    router.push('/schedule');
  }, [router]);

  return (
    <Layout>
      <Text>
        Redirecting you to schedule...
      </Text>
    </Layout>
  );
}
