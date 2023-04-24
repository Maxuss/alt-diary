import '@/styles/globals.css'
import type { AppProps } from 'next/app'
import { NextUIProvider, createTheme } from '@nextui-org/react';
import { ThemeProvider as NextThemesProvider } from 'next-themes';
import { SSRProvider } from '@react-aria/ssr';
import { Analytics } from '@vercel/analytics/react';
import { useSSR } from '@nextui-org/react'

export const lightTheme = createTheme({
  type: 'light',
  theme: {
    colors: {
      textLight: '#6d6d6d',

      primaryGradient: "linear-gradient(90deg, var(--color-sky-300) 0%, var(--color-blue-400) 80%)",
      secondaryGradient: "linear-gradient(90deg, var(--color-red-400) 0%, var(--color-amber-200) 60%)"
    },
  }
})

export const darkTheme = createTheme({
  type: 'dark',
  theme: {
    colors: {
      textLight: '#999999',

      primaryGradient: "linear-gradient(90deg, var(--color-indigo-500) 0%, var(--color-violet-500) 60%)",
      secondaryGradient: "linear-gradient(90deg, var(--color-orange-400) 0%, var(--color-amber-400) 60%)"
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
