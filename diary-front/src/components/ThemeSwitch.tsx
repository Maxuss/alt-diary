import { useTheme as useNextTheme } from 'next-themes'
import { Spacer, Switch, useTheme, Text } from '@nextui-org/react'
import { SunIcon } from '../icons/SunIcon';
import { MoonIcon } from '../icons/MoonIcon';

export default function ThemeSwitch() {
    const { setTheme } = useNextTheme();
    const { isDark, type, theme } = useTheme();

    return (
        <div>
            <Switch
                checked={isDark}
                size="xl"
                iconOn={<MoonIcon filled fill={theme?.colors.text.value} />}
                iconOff={<SunIcon filled fill={theme?.colors.text.value} />}
                onChange={(e) => setTheme(e.target.checked ? 'dark' : 'light')}
            />
        </div>
    )
}