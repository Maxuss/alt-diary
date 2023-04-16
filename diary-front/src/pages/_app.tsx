import '@/styles/globals.css'
import type { AppProps } from 'next/app'
import { NextUIProvider, createTheme } from '@nextui-org/react';
import { ThemeProvider as NextThemesProvider } from 'next-themes';
import { SSRProvider } from '@react-aria/ssr';
import { Analytics } from '@vercel/analytics/react';
import { useSSR } from '@nextui-org/react'

const lightTheme = createTheme({
  type: 'light',
  theme: {
    colors: {
      textLight: '#6d6d6d',
    },
  }
})

const darkTheme = createTheme({
  type: 'dark',
  theme: {
    colors: {
      textLight: '#999999',
    }
  }
})

function MyApp({ Component, pageProps }: AppProps) {
  const { isBrowser } = useSSR();

  /// SSR is very weird
  if (!isBrowser)
    return null;

  return (
    <NextThemesProvider
      defaultTheme="system"
      attribute="class"
      value={{
        light: lightTheme.className,
        dark: darkTheme.className,
      }}
    >
      <NextUIProvider>
        <Component {...pageProps} />
        <Analytics />
      </NextUIProvider>
    </NextThemesProvider>
  )
}

export default function App(props: AppProps) {
  return (
    <SSRProvider>
      <MyApp {...props} />
    </SSRProvider>
  )
}
