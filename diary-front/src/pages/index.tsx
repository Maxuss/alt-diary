import { Roboto } from 'next/font/google'
import Layout from '@/components/Layout'
import { Text } from '@nextui-org/react';
import { useEffect } from 'react';
import { useRouter } from 'next/router';
import { post } from '@/api/core';
import { refresh } from '@/api/auth';
import axios from 'axios';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    refresh().then(res => {
      if (res === true) {
        router.push("/schedule");
      } else {
        console.error(res);
        router.push("/student/login");
      }
    }).catch(err => {
      console.log(err)
      router.push("/student/login");
    })
  }, []);

  return (
    <Layout>
      <Text>
        Перенаправляем на расписание...
      </Text>
    </Layout>
  );
}
